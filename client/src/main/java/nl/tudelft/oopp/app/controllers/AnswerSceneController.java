package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.communication.QuestionCommunication;

import java.io.IOException;

public class AnswerSceneController {

    @FXML
    Button confirmButton;

    @FXML
    TextArea editTextArea;

    private String questionId;
    private String oldAnswer;
    private String userId;
    private QuestionCellController qcc;

    public void setQcc(QuestionCellController qcc) {
        this.qcc = qcc;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public void setOldText(String oldAnswer) {
        this.oldAnswer = oldAnswer;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Loades new answer window for the clicked question.
     * @param oldAnswer String old text of the question, to be edited
     * @throws IOException when the loader fails
     */
    public static void initialize(
            String oldAnswer, String questionId, String userId, QuestionCellController qcc)
            throws IOException {

        FXMLLoader loader = new FXMLLoader(AnswerSceneController
                .class.getResource("/AnswerQuestionScene.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        AnswerSceneController aqc = loader.<AnswerSceneController>getController();

        //set the fields for the newly created scene
        aqc.setQuestionId(questionId);
        aqc.setOldText(oldAnswer);
        aqc.setQcc(qcc);
        aqc.setUserId(userId);

        //put current questionText in the text area
        TextArea textArea = (TextArea) scene.lookup("#editTextArea");
        textArea.setText(oldAnswer);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Exits the Answer window.
     */
    public void exit() {
        Stage stage = (Stage) editTextArea.getScene().getWindow();
        stage.close();
    }

    /**
     * Confirms the changes to the  answer.
     */
    public void confirm() {
        String newText = editTextArea.getText();

        //if the text has changed
        if (!newText.equals(oldAnswer)) {
            QuestionCommunication.addAnswerText(questionId, newText, userId);
        }


        //close the window
        Stage stage = (Stage) editTextArea.getScene().getWindow();
        stage.close();

        //refresh the page
        qcc.refresh();
    }
}
