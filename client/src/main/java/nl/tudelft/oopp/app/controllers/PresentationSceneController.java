package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.models.Question;
import java.io.IOException;
import java.util.Collections;
import java.util.PriorityQueue;

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
     *
     * @throws InterruptedException - Thrown when a thread is waiting, sleeping,
     *                                or otherwise occupied, and the thread is interrupted,
     *                                either before or during the activity.
     */
    public void constantRefresh() throws InterruptedException {
        questions.clear();
        questions.addAll(HomeSceneCommunication.constantlyGetQuestions(session.getRoomLink()));
        loadQuestions();
    }

    @Override
    public void changeTheme(boolean mode) {
        String menuBackgroundAdd;
        String menuBackgroundRemove;
        if (mode) {
            menuBackgroundAdd = "darkMenuBackground";
            menuBackgroundRemove = "lightMenuBackground";
        } else {
            menuBackgroundAdd = "lightMenuBackground";
            menuBackgroundRemove = "darkMenuBackground";
        }

        mainMenu.getStyleClass().removeAll(Collections.singleton(menuBackgroundRemove));
        mainMenu.getStyleClass().add(menuBackgroundAdd);

        super.changeTheme(mode);
    }

    @Override
    protected void changeColours(String backgroundAdd, String backgroundRemove,
                                 String labelAdd, String labelRemove,
                                 String buttonAdd, String buttonRemove) {

        pane.getStyleClass().removeAll(Collections.singleton(backgroundRemove));
        pane.getStyleClass().add(backgroundAdd);

        for (Node child : mainMenu.getChildren()) {
            if (child instanceof Button) {
                child.getStyleClass().removeAll(Collections.singleton(buttonRemove));
                if (!child.getStyleClass().contains("menuBtnWhite")) {
                    child.getStyleClass().add(buttonAdd);
                }
            } else {
                child.getStyleClass().removeAll(Collections.singleton(labelRemove));
                child.getStyleClass().add(labelAdd);
            }
        }
    }
}
