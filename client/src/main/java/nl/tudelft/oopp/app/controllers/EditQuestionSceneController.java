package nl.tudelft.oopp.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.communication.QuestionCommunication;

import java.io.IOException;

public class EditQuestionSceneController {

    @FXML
    Button okButton;

    @FXML
    TextArea editTextArea;

    private String questionId;
    private String oldText;
    private QuestionCellController qcc;

    public void setQcc(QuestionCellController qcc) {
        this.qcc = qcc;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void setOldText(String oldText) {
        this.oldText = oldText;
    }

    /**
     * Loades new edit question window for the clicked question.
     * @param oldText String old text of the question, to be edited
     * @throws IOException when the loader fails
     */
    public static void init(String oldText, String questionId, QuestionCellController qcc)
            throws IOException {

        FXMLLoader loader = new FXMLLoader(EditQuestionSceneController
                .class.getResource("/editQuestionScene.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        EditQuestionSceneController eqc = loader.<EditQuestionSceneController>getController();

        //set the fields for the newly created scene
        eqc.setQuestionId(questionId);
        eqc.setOldText(oldText);
        eqc.setQcc(qcc);

        //put current questionText in the text area
        TextArea textArea = (TextArea) scene.lookup("#editTextArea");
        textArea.setText(oldText);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Handles click on 'Cancel' button.
     * closes the Edit question window.
     */
    public void cancelClicked() {
        Stage stage = (Stage) editTextArea.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles click on 'OK' button.
     * If the text was not edited just closes the window
     * otherwise sends a request (from QuestionCommunication) to change the question text
     */
    public void okClicked() {
        String newText = editTextArea.getText();

        //if the text has changed
        if (!newText.equals(oldText)) {
            QuestionCommunication.editQuestionText(questionId, newText);
        }

        //close the window
        Stage stage = (Stage) editTextArea.getScene().getWindow();
        stage.close();

        //refresh the page
        qcc.refresh();
    }


    public void starClicked() {
    }
}
