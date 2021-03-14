package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import nl.tudelft.oopp.app.communication.QuestionCommunication;

public class QuestionCellController {

    @FXML
    Button dismissButton;

    @FXML
    HBox questionCell;


    private HomeSceneController hsc;

    public void setHomeScene(HomeSceneController hsc) {
        this.hsc = hsc;
    }

    /**
     * dismisses the question.
     * deletes the database from the database and remove it from the screen
     */
    public void dismissClicked() {
        //get the id of the question to be deleted
        Node question = questionCell.getParent();
        String id = question.getId();

        //delete the question from the database
        QuestionCommunication.dismissQuestion(Long.parseLong(id));

        //remove the database from the screen
        hsc.deleteQuestionFromScene(id);
    }

}
