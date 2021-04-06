package nl.tudelft.oopp.app.controllers;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import nl.tudelft.oopp.app.communication.*;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.UserWarnedException;
import nl.tudelft.oopp.app.models.*;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class StudentPollSceneController extends StudentSceneController {

    @FXML
    public VBox pollBox;

    protected List<Poll> polls;


    /**
     * Create the poll options to fill the poll cell.
     * This is for options that were already answered and the poll closed.
     * The correct answers and the answered answers are reveled.
     *
     * @param pollAnswer  option data
     * @param optionCount place in the option order
     * @return a node to be loaded with the proper format and data
     * @throws IOException when the fxml file is not found
     */
    protected Node createPollAnswerCell(PollAnswer pollAnswer, int optionCount) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/studentPollAnswerCell.fxml"));
        Node newPollAnswer = loader.load();

        newPollAnswer.setId("" + pollAnswer.getId());

        Label optionNumber = (Label) newPollAnswer.lookup("#optionNumber");
        optionNumber.setText("Option " + optionCount);

        Label optionLabel = (Label) newPollAnswer.lookup("#optionText");
        optionLabel.setText(pollAnswer.getPollOption().getOptionText());

        VBox optionLabelBox = (VBox) newPollAnswer.lookup("#optionLabelBox");
        Label correctLabel = (Label) newPollAnswer.lookup("#correct");
        if (pollAnswer.getPollOption().isCorrect()) {
            correctLabel.setText("This option was correct");
            optionLabelBox.setStyle("-fx-border-color: #17aeda");
        } else {
            correctLabel.setText("This option was wrong");
            optionLabelBox.setStyle("-fx-border-color: #c32323");
        }

        Label scoredLabel = (Label) newPollAnswer.lookup("#scored");
        if (pollAnswer.getPollOption().isCorrect() == pollAnswer.isMarked()) {
            scoredLabel.setText("And you answered correctly");
            scoredLabel.setTextFill(Color.color(32,155,12));
        } else {
            scoredLabel.setText("And you answered it wrong :(");
            scoredLabel.setTextFill(Color.color(195,35,35));
        }
        scoredLabel.setText("And you answered "
                + (pollAnswer.getPollOption().isCorrect() == pollAnswer.isMarked()
                ? "correctly" : "wrong :("));

        return newPollAnswer;
    }

    /**
     * Create the poll options to fill the poll cell.
     * This is for options that were not answered and the poll closed.
     * The correct answers are reveled.
     *
     * @param pollOption  option data
     * @param optionCount place in the option order
     * @return a node to be loaded with the proper format and data
     * @throws IOException when the fxml file is not found
     */
    protected Node createPollUnansweredCell(
            PollOption pollOption,
            int optionCount) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/studentPollAnswerCell.fxml"));
        Node newPollAnswer = loader.load();

        newPollAnswer.setId("" + pollOption.getId());

        Label optionNumber = (Label) newPollAnswer.lookup("#optionNumber");
        optionNumber.setText("Option " + optionCount);

        Label optionLabel = (Label) newPollAnswer.lookup("#optionText");
        optionLabel.setText(pollOption.getOptionText());

        VBox optionLabelBox = (VBox) newPollAnswer.lookup("#optionLabelBox");
        Label correctLabel = (Label) newPollAnswer.lookup("#correct");
        if (pollOption.isCorrect()) {
            correctLabel.setText("This option was correct");
            optionLabelBox.getStyleClass().add("rightAnswer");
        } else {
            correctLabel.setText("This option was wrong");
            optionLabelBox.getStyleClass().add("wrongAnswer");
        }

        Label scoredLabel = (Label) newPollAnswer.lookup("#scored");
        scoredLabel.setText("You have not answered this poll in time");

        return newPollAnswer;
    }

    /**
     * Create the poll options to fill the poll cell.
     * This are the options that can still be answered
     *
     * @param pollOption option data
     * @return a node to be loaded with the proper format and data
     * @throws IOException when the fxml file is not found
     */
    protected Node createPollOptionCell(PollOption pollOption) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/studentPollOptionCell.fxml"));
        Node newPollOption = loader.load();

        newPollOption.setId("" + pollOption.getId());

        CheckBox optionLabel = (CheckBox) newPollOption.lookup("#isCorrect");
        optionLabel.setText(pollOption.getOptionText());

        return newPollOption;
    }

    /**
     * Create a new poll cell for a poll with its data.
     *
     * @param poll the poll to load the cell
     * @return a node to be loaded with the proper format and data
     * @throws IOException when the fxml file is not found
     */
    protected Node createPollCell(Poll poll) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/studentPollCell.fxml"));
        Node newPoll = loader.load();
        StudentPollCellController qsc = loader.getController();
        qsc.setHomeScene(this);

        newPoll.setId("" + poll.getId());

        Label questionlabel = (Label) newPoll.lookup("#questionText");
        questionlabel.setText(poll.getQuestion());
        VBox questionBox = (VBox) newPoll.lookup("#questionBox");
        questionBox.setPrefHeight(Region.USE_COMPUTED_SIZE);

        VBox pollOptionBox = (VBox) newPoll.lookup("#pollOptionBox");
        VBox resultBox = (VBox) newPoll.lookup("#resultBox");

        int optionCount = 1;
        if (poll.isFinished()) {

            resultBox.setVisible(true);
            List<PollAnswer> pollAnswers = PollCommunication.getAnswers(poll.id);

            boolean unanswered = pollAnswers.isEmpty();
            for (PollOption pollOption :
                    poll.getPollOptions()) {
                if (unanswered) {
                    pollOptionBox.getChildren().add(
                            createPollUnansweredCell(pollOption, optionCount));
                }

                int percentRight = (int) Math.round(pollOption.getScoreRate()*100);
                resultBox.getChildren().add(new Label("\n  Option " + optionCount
                        + ":\t\t" + percentRight + "%"));
                optionCount++;
            }

            optionCount = 1;
            for (PollAnswer pollAnswer :
                    pollAnswers) {
                pollOptionBox.getChildren().add(createPollAnswerCell(pollAnswer, optionCount));
                optionCount++;
            }

            Button submitAnswer = (Button) newPoll.lookup("#submitAnswer");
            submitAnswer.setVisible(false);
        } else {

            for (PollOption pollOption :
                    poll.getPollOptions()) {
                pollOptionBox.getChildren().add(createPollOptionCell(pollOption));
                optionCount++;
            }

        }

        return newPoll;
    }

    /**
     * Load polls to the scene, creating new nodes for the poll box.
     */
    public void loadPolls() {
        polls = PollCommunication.getPolls();

        pollBox.getChildren().clear();
        for (Poll poll :
                polls) {
            if (poll.isOpen()) {
                try {
                    pollBox.getChildren()
                            .add(createPollCell(poll));
                } catch (IOException e) {
                    System.out.println(e);
                    pollBox.getChildren().add(
                            new Label("Something went wrong while loading this poll"));
                }
            }
        }
    }

    /**
     * Update the polls list and load them to the scene.
     */
    public void refresh() {
        polls = new ArrayList<>();
        polls.addAll(PollCommunication.getPolls());
        loadPolls();
    }

    /**
     * This method is constantly called by a thread and refreshes the page.
     *
     * @throws ExecutionException           - may be thrown.
     * @throws InterruptedException         - may be thrown.
     * @throws NoStudentPermissionException - may be thrown.
     * @throws AccessDeniedException        - may be thrown.
     * @throws UserWarnedException          - may be thrown.
     */
    public void constantRefresh() throws ExecutionException, InterruptedException,
            NoStudentPermissionException, AccessDeniedException, UserWarnedException {
        polls = new ArrayList<>();
        polls.addAll(PollCommunication.getPolls());
        loadPolls();

        ServerCommunication.isRoomOpenStudents(session.getRoomLink());
        QuestionCommunication.updatesOnQuestions(session.getUserId(), session.getRoomLink());
        if (!session.isWarned()) {
            BanCommunication.isIpWarned(session.getRoomLink());
        } else {
            BanCommunication.isIpBanned(session.getRoomLink());
        }
    }
}

