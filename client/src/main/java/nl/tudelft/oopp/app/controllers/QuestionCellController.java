package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

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
     * dismisses the question
     */
    public void dismissClicked() {
        Node question = questionCell.getParent();
        String id = question.getId();
//        QuestionCommunication.dismissQuestion(Long.parseLong(id));
        hsc.deleteQuestion(id);
    }

}
