package nl.tudelft.oopp.app.controllers;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import nl.tudelft.oopp.app.models.Poll;
import nl.tudelft.oopp.app.models.PollOption;
import nl.tudelft.oopp.app.models.Session;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ModeratorPollSceneController extends ModeratorSceneController {

    @FXML
    private VBox pollBox;

    protected List<Poll> polls;

    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        refresh();
    }

    /**
     * Create the poll options to fill the poll cell.
     *
     * @param pollOption  option data
     * @param optionCount place in the option order
     * @return a node to be loaded with the proper format and data
     * @throws IOException when the fxml file is not found
     */
    protected Node createPollOptionCell(PollOption pollOption, int optionCount) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/moderatorPollOptionCell.fxml"));
        Node newPollOption = loader.load();

        newPollOption.setId("" + optionCount);

        TextArea questionTextArea = (TextArea) newPollOption.lookup("#optionText");
        Text questionLabel = (Text) newPollOption.lookup("#optionLabel");
        CheckBox optionIsCorrect = (CheckBox) newPollOption.lookup("#isCorrect");
        questionTextArea.setText(pollOption.getOptionText());
        questionLabel.setText("Option " + optionCount);
        optionIsCorrect.setSelected(pollOption.isCorrect());

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/moderatorPollCell.fxml"));
        Node newPoll = loader.load();
        ModeratorPollCellController qsc = loader.getController();
        qsc.setHomeScene(this);

        newPoll.setId("" + poll.getId());

        TextArea questionTextArea = (TextArea) newPoll.lookup("#questionText");
        questionTextArea.setText(poll.getQuestion());

        VBox pollOptionBox = (VBox) newPoll.lookup("#pollOptionBox");
        VBox resultBox = (VBox) newPoll.lookup("#resultBox");

        Text pollLabel = (Text) newPoll.lookup("#pollLabel");
        Button openButton = (Button) newPoll.lookup("#openPollButton");
        Button closeButton = (Button) newPoll.lookup("#closePollButton");

        if (poll.isFinished()) {
            pollLabel.setText("Finished Poll");
            openButton.setVisible(false);
            resultBox.setVisible(true);
        } else if (poll.isOpen()) {
            pollLabel.setText("Open Poll");
            closeButton.setVisible(true);
            openButton.setText("Change Poll");
        }

        int optionCount = 1;
        for (PollOption pollOption :
                poll.getPollOptions()) {
            pollOptionBox.getChildren().add(createPollOptionCell(pollOption, optionCount));

            if (poll.isFinished()) {
                resultBox.getChildren().add(new Label("  Option " + optionCount
                        + ":\t\t" + pollOption.getScoreRate() + "\n"));
            }

            optionCount++;
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
            try {
                pollBox.getChildren()
                        .add(createPollCell(poll));
            } catch (IOException e) {
                pollBox.getChildren().add(
                        new Label("Something went wrong while loading this poll"));
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
     * @throws InterruptedException - Thrown when a thread is waiting, sleeping,
     *                                or otherwise occupied, and the thread is interrupted,
     *                                either before or during the activity.
     */
    public void constantRefresh() throws InterruptedException {

//        polls = new ArrayList<>();
//        polls.addAll(PollCommunication.constantlyGetPolls(session.getRoomLink()));
//        loadPolls();
//        String linkId = session.getRoomLink();
//        try {
//            ServerCommunication.isRoomOpenStudents(linkId);
//            changeImageOpenRoomButton();
//        } catch (NoStudentPermissionException exception) {
//            changeImageCloseRoomButton();
//        }

    }

    /**
     * Create a new empty poll on the server.
     */
    public void createPoll() {
        long pollId = PollCommunication.createPoll();
        refresh();
    }
}