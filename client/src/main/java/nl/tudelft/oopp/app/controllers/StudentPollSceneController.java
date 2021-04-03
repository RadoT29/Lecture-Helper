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
import javafx.scene.layout.VBox;
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

public class StudentPollSceneController implements Initializable {

    @FXML
    public Button reactionButton;
    @FXML
    public Button speedButton;
    @FXML
    public VBox speedMenu;
    @FXML
    public VBox reactionMenu;

    @FXML
    public Button fastButton;
    @FXML
    public Button okButton;
    @FXML
    public Button slowButton;

    @FXML
    public Button happyButton;
    @FXML
    public Button sadButton;
    @FXML
    public Button confusedButton;

    @FXML
    public VBox pollBox;

    protected List<Poll> polls;
    Session session = Session.getInstance();

    private TranslateTransition openSpeedNav;
    private TranslateTransition closeSpeedNav;
    private TranslateTransition closeSpeedFastNav;

    private TranslateTransition openReactionNav;
    private TranslateTransition closeReactionNav;
    private TranslateTransition closeReactionFastNav;

    /**
     * This method initializes the state of the navigation bar.
     * It hides the sliding bars behind the regular one.
     *
     * @param url - The path.
     * @param rb  - Provides any needed resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        openSpeedNav = new TranslateTransition(Duration.millis(100), speedMenu);
        openSpeedNav.setToX(speedMenu.getTranslateX() - speedMenu.getWidth());
        closeSpeedNav = new TranslateTransition(Duration.millis(100), speedMenu);
        closeSpeedFastNav = new TranslateTransition(Duration.millis(.1), speedMenu);

        openReactionNav = new TranslateTransition(Duration.millis(100), reactionMenu);
        openReactionNav.setToX(reactionMenu.getTranslateX() - reactionMenu.getWidth());
        closeReactionNav = new TranslateTransition(Duration.millis(100), reactionMenu);
        closeReactionFastNav = new TranslateTransition(Duration.millis(.1), reactionMenu);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                closeSpeedFastNav.setToX(-(speedMenu.getWidth()));
                closeSpeedFastNav.play();
                closeReactionFastNav.setToX(-(reactionMenu.getWidth()));
                closeReactionFastNav.play();
            }
        });

    }

    /**
     * This method closes the sliding part of the speed bar.
     */
    public void hideSpeedBar() {
        speedButton.getStyleClass().remove("speedBtnWhite");
        speedButton.getStyleClass().add("speedBtnBlack");
        closeSpeedNav.setToX(-(speedMenu.getWidth()));
        closeSpeedNav.play();
    }

    /**
     * This method checks the current state of the speed bar.
     * Afterwards, it decides whether to close or open the speed bar.
     */
    public void controlSpeedMenu() {
        if ((reactionMenu.getTranslateX()) != -(reactionMenu.getWidth())) {
            hideReactionBar();
        }
        if ((speedMenu.getTranslateX()) == -(speedMenu.getWidth())) {
            speedButton.getStyleClass().remove("speedBtnBlack");
            speedButton.getStyleClass().add("speedBtnWhite");
            openSpeedNav.play();
        } else {
            hideSpeedBar();
        }
    }

    /**
     * This method closes the sliding part of the reaction bar.
     */
    public void hideReactionBar() {

        reactionButton.getStyleClass().remove("reactionBtnWhite");
        reactionButton.getStyleClass().add("reactionBtnBlack");
        closeReactionNav.setToX(-(reactionMenu.getWidth()));
        closeReactionNav.play();
    }

    /**
     * This method checks the current state of the reaction bar.
     * Afterwards, it decides whether to close or open the reaction bar.
     */
    public void controlReactionMenu() {
        if ((speedMenu.getTranslateX()) != -(speedMenu.getWidth())) {
            hideSpeedBar();
        }
        if ((reactionMenu.getTranslateX()) == -(reactionMenu.getWidth())) {
            reactionButton.getStyleClass().remove("reactionBtnBlack");
            reactionButton.getStyleClass().add("reactionBtnWhite");
            openReactionNav.play();
        } else {
            hideReactionBar();
        }
    }

    /**
     * This method sends the corresponding SpeedReaction when the fastButton is clicked.
     */
    public void tooFast() {
        ReactionCommunication.postReaction(new SpeedReaction(1));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        fastButton.setDisable(true);
        okButton.setDisable(false);
        slowButton.setDisable(false);
    }


    /**
     * This method sends the corresponding SpeedReaction when the okButton is clicked.
     */
    public void fastEnough() {
        ReactionCommunication.postReaction(new SpeedReaction(0));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        fastButton.setDisable(false);
        okButton.setDisable(true);
        slowButton.setDisable(false);
    }

    /**
     * This method sends the corresponding SpeedReaction when the slowButton is clicked.
     */
    public void tooSlow() {
        ReactionCommunication.postReaction(new SpeedReaction(-1));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        fastButton.setDisable(false);
        okButton.setDisable(false);
        slowButton.setDisable(true);
    }

    /**
     * This method sends the corresponding EmotionReaction when the happyButton is clicked.
     */
    public void addHappy() {
        ReactionCommunication.postReaction(new EmotionReaction(1));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        happyButton.setDisable(true);
        sadButton.setDisable(false);
        confusedButton.setDisable(false);
    }

    /**
     * This method sends the corresponding EmotionReaction when the sadButton is clicked.
     */
    public void addSad() {
        ReactionCommunication.postReaction(new EmotionReaction(0));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        happyButton.setDisable(false);
        sadButton.setDisable(true);
        confusedButton.setDisable(false);
    }

    /**
     * This method sends the corresponding EmotionReaction when the confusedButton is clicked.
     */
    public void addConfused() {
        ReactionCommunication.postReaction(new EmotionReaction(-1));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        happyButton.setDisable(false);
        sadButton.setDisable(false);
        confusedButton.setDisable(true);
    }

    /**
     * will be removed with refactoring.
     *
     * @throws IOException k
     */
    public void goToPolls() throws IOException {
        Parent loader = new FXMLLoader(getClass().getResource("/studentPollScene.fxml")).load();
        Stage stage = (Stage) reactionMenu.getScene().getWindow();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.8;
        double height = screenSize.getHeight() * 0.8;

        Scene scene = new Scene(loader, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    //Poll stuff

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

        Label correctLabel = (Label) newPollAnswer.lookup("#correct");
        correctLabel.setText("This option was "
                + (pollAnswer.getPollOption().isCorrect() ? "correct" : "wrong"));

        Label scoredLabel = (Label) newPollAnswer.lookup("#scored");
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

        Label correctLabel = (Label) newPollAnswer.lookup("#correct");
        correctLabel.setText("This option was "
                + (pollOption.isCorrect() ? "correct" : "wrong"));

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

        Label optionLabel = (Label) newPollOption.lookup("#optionText");
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


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.4;
        questionlabel.setPrefWidth(width);
        questionlabel.setMaxWidth(width);

        VBox pollOptionBox = (VBox) newPoll.lookup("#pollOptionBox");
        VBox resultBox = (VBox) newPoll.lookup("#resultBox");

        int optionCount = 1;
        if (poll.isFinished()) {

            List<PollAnswer> pollAnswers = PollCommunication.getAnswers(poll.id);

            boolean unanswered = pollAnswers.isEmpty();
            for (PollOption pollOption :
                    poll.getPollOptions()) {
                if (unanswered) {
                    pollOptionBox.getChildren().add(
                            createPollUnansweredCell(pollOption, optionCount));
                }
                resultBox.getChildren().add(new Label("Option " + optionCount
                        + ":     " + pollOption.getScoreRate()));
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
        polls.addAll(PollCommunication.constantlyGetPolls(session.getRoomLink()));
        loadPolls();
        //        if (session.getIsModerator()) {
        //            String linkId = session.getRoomLink();
        //            try {
        //                ServerCommunication.isRoomOpenStudents(linkId);
        //                //changeImageOpenRoomButton();
        //            } catch (NoStudentPermissionException exception) {
        //                //changeImageCloseRoomButton();
        //                exception.getStackTrace();
        //            }
        //        }

        //ServerCommunication.isTheRoomClosed(session.getRoomLink());
        //if (!session.getIsModerator()) {
        //ServerCommunication.hasStudentPermission(session.getRoomLink());
        ServerCommunication.isRoomOpenStudents(session.getRoomLink());
        QuestionCommunication.updatesOnQuestions(session.getUserId(), session.getRoomLink());
        if (!session.isWarned()) {
            BanCommunication.isIpWarned(session.getRoomLink());
        } else {
            BanCommunication.isIpBanned(session.getRoomLink());
        }
        // }


    }
}

