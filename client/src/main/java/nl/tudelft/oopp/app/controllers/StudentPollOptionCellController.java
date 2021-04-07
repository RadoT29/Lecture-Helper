package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import nl.tudelft.oopp.app.models.Session;

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
