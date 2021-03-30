package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.communication.FeedbackCommunication;
import nl.tudelft.oopp.app.models.Feedback;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewFeedbackSceneController {

    public static void init() throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewFeedbackSceneController
                .class.getResource("/viewFeedbackScene.fxml"));
        Parent parent = loader.load();
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.4;
        double height = screenSize.getHeight() * 0.5;
        Scene scene = new Scene(parent, width, height);



        List<Feedback> feedbacks = FeedbackCommunication.getFeedback();

        StringBuilder sb = new StringBuilder();
        for (Feedback f: feedbacks) {
            sb.append("\n\n");
            sb.append(f.toString());
            sb.append("\n\n");
        }
        String text = sb.toString();
        //put current questionText in the text area
        ScrollPane sp = (ScrollPane) scene.lookup("#feedbackScroll");
        Text textFiled = (Text) sp.getContent().lookup("#feedbackText");
        textFiled.setText(text);
        textFiled.setWrappingWidth(width - 50);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
