package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nl.tudelft.oopp.app.communication.PollCommunication;
import nl.tudelft.oopp.app.models.Poll;
import nl.tudelft.oopp.app.models.PollOption;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PollCellController {

    @FXML
    TextArea questionText;

    @FXML
    private VBox pollOptionBox;

    @FXML
    private VBox pollBox;


    private PollSceneController hsc;

    public void setHomeScene(PollSceneController hsc) {
        this.hsc = hsc;
    }

    public void addOption() throws IOException {
        // load the poll to a newNode and set it's homeSceneController to this
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pollOptionCellModerator.fxml"));
        Node newPollOption = loader.load();

        //set the node id to the poll id
        newPollOption.setId("pollOptionId" + (pollOptionBox.getChildren().size() + 1));

        //set the poll text
        Text questionLabel = (Text) newPollOption.lookup("#optionLabel");
        questionLabel.setText("Option " + (pollOptionBox.getChildren().size() + 1));

        pollOptionBox.getChildren().add(newPollOption);
    }

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
    }

    public void closePoll() {
        long pollId = Long.parseLong(pollBox.getParent().getId());
        PollCommunication.closePoll(pollId);
    }

}
