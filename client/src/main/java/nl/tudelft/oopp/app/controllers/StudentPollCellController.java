package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.app.communication.PollCommunication;
import nl.tudelft.oopp.app.models.Poll;
import nl.tudelft.oopp.app.models.PollOption;

import java.util.ArrayList;
import java.util.List;

public class StudentPollCellController {

    @FXML
    Label questionText;

    @FXML
    private VBox pollOptionBox;

    @FXML
    private HBox pollBox;


    private StudentPollSceneController hsc;

    public void setHomeScene(StudentPollSceneController hsc) {
        this.hsc = hsc;
    }

    /**
     * Send this polls answers to the server.
     */
    public void submitAnswers() {
        Poll poll = new Poll();

        List<PollOption> pollOptions = new ArrayList<>();
        for (Node node :
                pollOptionBox.getChildren()) {
            CheckBox optionIsCorrect = (CheckBox) node.lookup("#isCorrect");

            PollOption pollOption = new PollOption();
            pollOption.setId(Long.parseLong(node.getId()));
            pollOption.setCorrect(optionIsCorrect.isSelected());

            pollOptions.add(pollOption);
        }
        long pollId = Long.parseLong(pollBox.getParent().getId());
        poll.setId(pollId);
        poll.setPollOptions(pollOptions);


        PollCommunication.sendAnswers(poll);
    }

}
