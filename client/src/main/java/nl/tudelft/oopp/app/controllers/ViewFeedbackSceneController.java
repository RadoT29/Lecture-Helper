package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import nl.tudelft.oopp.app.communication.FeedbackCommunication;
import nl.tudelft.oopp.app.models.Feedback;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ViewFeedbackSceneController {

    @FXML
    Button closeButton;

    /**
     * initializes a View Feedback Window.
     * gets the feedback for the room from the server and displays it in the scene
     * @throws IOException when the loader fails
     */
    public static void init() throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewFeedbackSceneController
                .class.getResource("/viewFeedbackScene.fxml"));
        Parent parent = loader.load();

        //window size
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.4;
        double height = screenSize.getHeight() * 0.5;

        Scene scene = new Scene(parent, width, height);

        String feedbackString = getFeedbackString();

        //set the text in the scene
        ScrollPane sp = (ScrollPane) scene.lookup("#feedbackScroll");
        Text text = (Text) sp.getContent().lookup("#feedbackText");
        text.setText(feedbackString);
        text.setWrappingWidth(width - 50);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    /**
     * get the list of Feedback from the server for the room
     * and transforms it to a String.
     * @return String representation of the room's feedback
     */
    private static String getFeedbackString() {
        List<Feedback> feedbacks = FeedbackCommunication.getFeedback();
        StringBuilder sb = new StringBuilder();
        for (Feedback f: feedbacks) {
            sb.append("\n\n");
            sb.append(f.toString());
            sb.append("\n\n");
        }
        return sb.toString();
    }


    /**
     * handles click on the close button.
     * Closes the window
     */
    public void closeClicked() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
