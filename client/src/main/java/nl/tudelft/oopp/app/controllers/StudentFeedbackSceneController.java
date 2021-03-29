package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.models.Session;

import java.io.IOException;


public class StudentFeedbackSceneController {

    @FXML
    TextArea editTextArea;


    /**
     * Loads new feedback question window.
     */
    public static void init() {
        try {
            Session session = Session.getInstance();
            FXMLLoader loader = new FXMLLoader(StudentFeedbackSceneController
                    .class.getResource("/editQuestionScene.fxml"));
            loader.setControllerFactory((Class<?> controllerType) -> {
                return new StudentFeedbackSceneController();
            });
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            //set promptText in the textArea
            TextArea textArea = (TextArea) scene.lookup("#editTextArea");
            textArea.setPromptText("What did you think of the room \""
                    + session.getRoomName() + "\"?");

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.print("Something went wrong while loading the feedback scene");
        }


    }

    /**
     * Handles click on 'Cancel' button.
     * closes the feedback window.
     */
    public void cancelClicked() {
        Stage stage = (Stage) editTextArea.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles click on 'OK' button.
     * If there is no text just closes the window
     * otherwise sends a request to post a comment
     */
    public void okClicked() {
        Session session = Session.getInstance();
        String comment = editTextArea.getText();

        //if the text has changed
        if (!comment.equals("")) {
            HomeSceneCommunication.sendFeedback(session.getRoomLink(), comment);
        }

        //close the window
        Stage stage = (Stage) editTextArea.getScene().getWindow();
        stage.close();
    }

}
