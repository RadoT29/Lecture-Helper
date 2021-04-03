package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.UserWarnedException;
import nl.tudelft.oopp.app.models.Question;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.PriorityQueue;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * This class controls the Main scene of the Moderators.
 */
public class PresentationSceneController extends SceneController implements Initializable {
    @FXML
    private VBox questionBox;
    @FXML
    public VBox mainMenu;

    /**
     * Method to load questions.
     */
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

    public void goToHome() throws IOException {
        changeScene("/moderatorMainScene.fxml", 0.8);
    }

    /**
     * Refresh method.
     */
    public void refresh() {
        questions = new PriorityQueue<>();
        questions.addAll(HomeSceneCommunication.getQuestions());
        loadQuestions();
    }

    /**
     * Method to constant refresh .
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws NoStudentPermissionException
     * @throws AccessDeniedException
     * @throws UserWarnedException
     */
    public void constantRefresh() throws ExecutionException, InterruptedException,
            NoStudentPermissionException, AccessDeniedException, UserWarnedException {
        questions.clear();
        questions.addAll(HomeSceneCommunication.constantlyGetQuestions(session.getRoomLink()));
        loadQuestions();
    }
}
