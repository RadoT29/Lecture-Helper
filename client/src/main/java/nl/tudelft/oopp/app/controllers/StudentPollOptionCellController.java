package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.app.communication.PollCommunication;
import nl.tudelft.oopp.app.models.Poll;
import nl.tudelft.oopp.app.models.PollOption;
import nl.tudelft.oopp.app.models.Session;

import java.util.ArrayList;
import java.util.List;

public class StudentPollOptionCellController {

    @FXML
    private CheckBox isCorrect;

    List<String> answersMarked = Session.getInstance().getPollsAnswersMarked();

    /**
     * Send this polls answers to the server.
     */
    public void markAsAnswered() {
        String nodeId = isCorrect.getParent().getParent().getId();
        if (isCorrect.isSelected()) {
            answersMarked.add(nodeId);
        } else {
            answersMarked.remove(nodeId);
        }
    }

}
