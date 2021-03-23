package nl.tudelft.oopp.app.controllers;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.models.Session;

/**
 * This class controls the Main scene of the Moderators.
 */
public class ModeratorSceneController extends HomeSceneController implements Initializable {
    @FXML
    public Button menuButton;
    @FXML
    public VBox mainMenu;
    @FXML
    public VBox slidingMenu;

    private TranslateTransition openNav;
    private TranslateTransition closeNav;
    private TranslateTransition closeFastNav;

    /**
     * This method initializes the state of the navigation bar.
     * It hides the sliding bar behind the regular one.
     * @param url - The path.
     * @param rb - Provides any needed resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        openNav = new TranslateTransition(Duration.millis(100), slidingMenu);
        openNav.setToX(slidingMenu.getTranslateX() - slidingMenu.getWidth());
        closeNav = new TranslateTransition(Duration.millis(100), slidingMenu);
        closeFastNav = new TranslateTransition(Duration.millis(.1), slidingMenu);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                closeFastNav.setToX(-(slidingMenu.getWidth()));
                closeFastNav.play();
            }
        });

        super.initialize(url, rb);
    }

    /**
     * This method closes the sliding part of the navigation bar.
     */
    public void hideSlidingBar() {

        menuButton.getStyleClass().remove("menuBtnWhite");
        menuButton.getStyleClass().add("menuBtnBlack");
        closeNav.setToX(-(slidingMenu.getWidth()));
        closeNav.play();
    }

    /**
     * This method checks the current state of the navigation bar.
     * Afterwards, it decides whether to close or open the navigation bar.
     */
    public void controlMenu() {

        if ((slidingMenu.getTranslateX()) == -(slidingMenu.getWidth())) {
            menuButton.getStyleClass().remove("menuBtnBlack");
            menuButton.getStyleClass().add("menuBtnWhite");
            openNav.play();
        } else {
            hideSlidingBar();
        }
    }

    /**
     * close the room.
     */
    public void closeRoom() {
        super.closeRoom();
    }

    /**
     * kick all students.
     */
    public void kickAllStudents() {
        super.kickAllStudents();
    }

    /**
     * Pressing the sendButton will send all the text in the questionInput
     * to the sever as a Question object.
     */
    public void sendQuestion() {
        super.sendQuestion();
    }

    /**
     * fill in the priority queue and and load them on the screen.
     */
    public void refresh() {
        super.refresh();
    }

    /**
     * Method to clear all questions and allow the moderator to reset the room
     * It will activate when the clear questions button is clicked (within the moderator view)
     * It will reconfirm if the user is a moderator and call clearQuestions
     * in the HomeCommunicationScene.
     */
    public void clearQuestionsClicked() {
        Session session = Session.getInstance();

        if (session.getIsModerator()) {
            HomeSceneCommunication.clearQuestions(session.getRoomLink());
        }

        refresh();
    }

    /**
     * Method to load the presentation mode scene.
     * Makes the scene smaller so it takes less space on the lecturer screen
     * @throws IOException if it cant load the fxml file
     */
    public void presenterMode() throws IOException {
        Parent loader = new FXMLLoader(getClass().getResource("/presentationScene.fxml")).load();
        Stage stage = (Stage) mainMenu.getScene().getWindow();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.65;
        double height = screenSize.getHeight() * 0.6;

        Scene scene = new Scene(loader, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * This method opens the scene where are inserted the
     * number of questions per time.
     * @throws IOException - may thrown
     */
    public void openConstraintsScene() throws IOException {
        QuestionsPerTimeController questionsPerTimeController = new QuestionsPerTimeController();
        questionsPerTimeController.open();

    }
}
