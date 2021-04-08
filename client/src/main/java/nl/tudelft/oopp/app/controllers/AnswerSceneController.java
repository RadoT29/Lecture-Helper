package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import nl.tudelft.oopp.app.communication.QuestionCommunication;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import lombok.Setter;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.UserWarnedException;

@Setter
public class AnswerSceneController {

    @FXML
    Button confirmButton;

    @FXML
    TextArea editTextArea;

    private String questionId;
    private String oldAnswer;
    private String userId;
    private boolean type;
    private SceneController sc;


    /**
     * Loads new answer window for the clicked question.
     * @param oldAnswer String old text of the question, to be edited
     * @throws IOException when the loader fails
     */
    public static void initialize(
            String oldAnswer, String questionId, String userId, boolean type, SceneController sc)
            throws IOException {

        //Load scene.
        FXMLLoader loader = new FXMLLoader(AnswerSceneController
                .class.getResource("/AnswerQuestionScene.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        AnswerSceneController aqc = loader.getController();

        //set the fields for the newly created scene
        aqc.setQuestionId(questionId);
        aqc.setOldAnswer(oldAnswer);
        aqc.setUserId(userId);
        aqc.setType(type);
        aqc.setSc(sc);

        //put current answerText in the text area
        TextArea textArea = (TextArea) scene.lookup("#editTextArea");
        textArea.setText(oldAnswer);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
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
            QuestionCommunication.addAnswerText(questionId, newText, userId, type);
            try {
                sc.constantRefresh();
            } catch (ExecutionException | InterruptedException
                    | NoStudentPermissionException | AccessDeniedException
                    | UserWarnedException e) {
                e.printStackTrace();
            }

        }
        Stage stage = (Stage) editTextArea.getScene().getWindow();
        stage.close();
    }
}
