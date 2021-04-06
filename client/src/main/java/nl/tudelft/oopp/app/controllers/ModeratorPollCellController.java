package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.app.communication.PollCommunication;
import nl.tudelft.oopp.app.models.Poll;
import nl.tudelft.oopp.app.models.PollOption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModeratorPollCellController {

    @FXML
    TextArea questionText;

    @FXML
    private VBox pollOptionBox;

    @FXML
    private HBox pollBox;


    private ModeratorPollSceneController mpsc;

    public void setHomeScene(ModeratorPollSceneController mpsc) {
        this.mpsc = mpsc;
    }

    /**
     * Add a new option for the poll.
     * @throws IOException when the fxml file is not found
     */
    public void addOption() throws IOException {
        // load the poll to a newNode and set it's homeSceneController to this
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/moderatorPollOptionCell.fxml"));
        Node newPollOption = loader.load();

        //set the node id to the poll id
        newPollOption.setId("pollOptionId" + (pollOptionBox.getChildren().size() + 1));

        //set the poll text
        Text questionLabel = (Text) newPollOption.lookup("#optionLabel");
        questionLabel.setText("Option " + (pollOptionBox.getChildren().size() + 1));

        pollOptionBox.getChildren().add(newPollOption);

        mpsc.refresh();
    }


    /**
     * Send the poll data to be updated in the server.
     * Also opens the poll to be answered.
     */
    public void sendPoll() {
        Poll poll = new Poll();

        List<PollOption> pollOptions = new ArrayList<>();
        for (Node node :
                pollOptionBox.getChildren()) {
            TextArea optionText = (TextArea) node.lookup("#optionText");
            CheckBox optionIsCorrect = (CheckBox) node.lookup("#isCorrect");


            PollOption pollOption = new PollOption();
            pollOption.setOptionText(optionText.getText());
            pollOption.setCorrect(optionIsCorrect.isSelected());

            pollOptions.add(pollOption);
        }

        poll.setQuestion(questionText.getText());
        poll.setPollOptions(pollOptions);

        long pollId = Long.parseLong(pollBox.getParent().getId());
        PollCommunication.updatePoll(pollId, poll);

        mpsc.refresh();
    }

    /**
     * Set the poll as finished so it cannot be answered by students
     * and everyone can see the results.
     */
    public void closePoll() {
        long pollId = Long.parseLong(pollBox.getParent().getId());
        PollCommunication.finishPoll(pollId);

        mpsc.refresh();
    }

}
