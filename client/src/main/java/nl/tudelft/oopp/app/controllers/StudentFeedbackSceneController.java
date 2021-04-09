package nl.tudelft.oopp.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.communication.FeedbackCommunication;
import nl.tudelft.oopp.app.models.Feedback;
import nl.tudelft.oopp.app.models.Session;
import nl.tudelft.oopp.app.scenes.LimitedTextArea;

import java.io.IOException;


public class StudentFeedbackSceneController {

    @FXML
    TextArea editTextArea;


    @FXML
    HBox starBox;

    private Button[] stars;
    private int rating;

    /**
     * Loads new feedback question window.
     */
    public static void init() {
        try {
            Session session = Session.getInstance();
            FXMLLoader loader = new FXMLLoader(StudentFeedbackSceneController
                    .class.getResource("/editQuestionScene.fxml"));
            loader.setControllerFactory((Class<?> controllerType) ->
                    new StudentFeedbackSceneController());
            Parent parent = loader.load();

            Scene scene = new Scene(parent);
            //show stars
            HBox starBox = (HBox) scene.lookup("#starBox");
            starBox.setVisible(true);
            //set promptText in the textArea
            LimitedTextArea textArea = (LimitedTextArea) scene.lookup("#editTextArea");
            textArea.setPromptText("What did you think of the room \""
                    + session.getRoomName() + "\"? (max 255 characters)");

            Stage stage = new Stage();
            stage.setTitle("Leave feedback");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.print("Something went wrong while loading the feedback scene");
        }
    }



    /**
     * Handles click on 'OK' button.
     * If there is no text just closes the window
     * otherwise sends a request to post a comment
     */
    public void okClicked() {
        String comment = editTextArea.getText();

        //if the text has changed or the rating was chosen
        if (!comment.equals("") || rating != 0) {
            Feedback feedback = new Feedback(comment, rating);
            FeedbackCommunication.sendFeedback(feedback);
        }

        //close the window
        Stage stage = (Stage) editTextArea.getScene().getWindow();
        stage.close();
    }


    /**
     * handles a click on a star.
     * colors the stars and sets the rating variable to the corresponding value;
     * @param event ActionEvent event that called that method
     */
    public void starClicked(ActionEvent event) {
        //initialize stars if needed
        if (stars == null) {
            findStars();
        }
        //find value
        Node node = (Node) event.getSource();
        String userData = (String) node.getUserData();
        int value = Integer.parseInt(userData);

        //set rating
        rating = value;

        //color stars
        for (int i = 0; i < stars.length; i++) {
            if (i < value) {
                stars[i].getStyleClass().set(1, "starFilled");
            } else {
                stars[i].getStyleClass().set(1, "starEmpty");
            }
        }
    }


    /**
     * initializes stars array and adds the 5 stars to it.
     */
    private void findStars() {
        stars = new Button[5];
        stars[0] = (Button) starBox.lookup("#starOne");
        stars[1] = (Button) starBox.lookup("#starTwo");
        stars[2] = (Button) starBox.lookup("#starThree");
        stars[3] = (Button) starBox.lookup("#starFour");
        stars[4] = (Button) starBox.lookup("#starFive");

    }

}
