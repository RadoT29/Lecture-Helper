package nl.tudelft.oopp.app.controllers;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.models.Moderator;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.PriorityQueue;
import java.util.ResourceBundle;

/**
 * This class controls the Main scene of the Moderators.
 */
public class PresentationSceneController extends ModeratorSceneController implements Initializable {
    @FXML
    private VBox questionBox;
    @FXML
    public VBox mainMenu;

    @Override
    public void loadQuestions() {

        String resource = "/questionCellPresentation.fxml";

        questionBox.getChildren().clear();

        for (int i = 0; i < 5; i++) {
            Question question = questions.poll();
            if (question == null) {
                break;
            }
            try {
                questionBox.getChildren()
                        .add(QuestionCellController.init(question, resource, this));
            } catch (IOException e) {
                questionBox.getChildren().add(
                        new Label("Something went wrong while loading this question"));
            }
        }
    }

    /**
     * fill in the priority queue and and load them on the screen.
     */
    public void refresh() {
        super.refresh();
    }

    public void presenterMode() throws IOException {

    }

    /**
     * Method to load the main moderator scene.
     * Makes the scene bigger so the moderator can interacat with all its features
     * @throws IOException if it cant load the fxml file
     */
    public void goToHome() throws IOException {
        Parent loader = new FXMLLoader(getClass().getResource("/moderatorScene.fxml")).load();
        Stage stage = (Stage) mainMenu.getScene().getWindow();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.8;
        double height = screenSize.getHeight() * 0.8;

        Scene scene = new Scene(loader, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }


    /**
     * Method to make it so that when the scene loads
     * the refresh method is automatically executed.
     * @param url - url of the scene
     * @param rb - resource bundle used
     */

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
