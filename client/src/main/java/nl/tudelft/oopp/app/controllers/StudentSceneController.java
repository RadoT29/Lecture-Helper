package nl.tudelft.oopp.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nl.tudelft.oopp.app.communication.ReactionCommunication;
import nl.tudelft.oopp.app.models.EmotionReaction;
import nl.tudelft.oopp.app.models.QuestionsUpdate;
import nl.tudelft.oopp.app.models.SpeedReaction;

/**
 * This class controls the Main scene of the Students.
 */
public abstract class StudentSceneController extends SceneController {

    @FXML
    public Button reactionButton;
    @FXML
    public Button speedButton;
    @FXML
    public VBox speedMenu;
    @FXML
    public VBox reactionMenu;
    @FXML
    public VBox mainMenu;

    @FXML
    public Button fastButton;
    @FXML
    public Button okButton;
    @FXML
    public Button slowButton;

    @FXML
    public Button happyButton;
    @FXML
    public Button sadButton;
    @FXML
    public Button confusedButton;


    private TranslateTransition openSpeedNav;
    private TranslateTransition closeSpeedNav;

    private TranslateTransition openReactionNav;
    private TranslateTransition closeReactionNav;

    /**
     * This method initializes the state of the navigation bar.
     * It hides the sliding bars behind the regular one.
     * @param url - The path.
     * @param rb - Provides any needed resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        openSpeedNav = new TranslateTransition(Duration.millis(150), speedMenu);
        openSpeedNav.setToX(speedMenu.getLayoutX());
        closeSpeedNav = new TranslateTransition(Duration.millis(150), speedMenu);

        openReactionNav = new TranslateTransition(Duration.millis(150), reactionMenu);
        openReactionNav.setToX(speedMenu.getLayoutX());
        closeReactionNav = new TranslateTransition(Duration.millis(150), reactionMenu);

        super.initialize(url,rb);
    }

    /**
     * This method closes the sliding part of the speed bar.
     */
    public void hideSpeedBar() {
        speedButton.getStyleClass().removeAll(Collections.singleton("menuBtnWhite"));
        speedButton.getStyleClass().add(buttonColour);
        closeSpeedNav.setToX(-(speedMenu.getWidth()));
        closeSpeedNav.play();
    }

    /**
     * This method checks the current state of the speed bar.
     * Afterwards, it decides whether to close or open the speed bar.
     */
    public void controlSpeedMenu() {
        if ((reactionMenu.getTranslateX()) != -(reactionMenu.getWidth())) {
            hideReactionBar();
        }
        if ((speedMenu.getTranslateX()) == -(speedMenu.getWidth())) {
            speedButton.getStyleClass().removeAll(Collections.singleton(buttonColour));
            speedButton.getStyleClass().add("menuBtnWhite");
            openSpeedNav.play();
        } else {
            hideSpeedBar();
        }
    }

    /**
     * This method closes the sliding part of the reaction bar.
     */
    public void hideReactionBar() {
        reactionButton.getStyleClass().removeAll(Collections.singleton("menuBtnWhite"));
        reactionButton.getStyleClass().add(buttonColour);
        closeReactionNav.setToX(-(reactionMenu.getWidth()));
        closeReactionNav.play();
    }

    /**
     * This method checks the current state of the reaction bar.
     * Afterwards, it decides whether to close or open the reaction bar.
     */
    public void controlReactionMenu() {
        if ((speedMenu.getTranslateX()) != -(speedMenu.getWidth())) {
            hideSpeedBar();
        }
        if ((reactionMenu.getTranslateX()) == -(reactionMenu.getWidth())) {
            reactionButton.getStyleClass().removeAll(Collections.singleton(buttonColour));
            reactionButton.getStyleClass().add("menuBtnWhite");
            openReactionNav.play();
        } else {
            hideReactionBar();
        }
    }

    /**
     * This method sends the corresponding SpeedReaction when the fastButton is clicked.
     */
    public void tooFast() {
        ReactionCommunication.postReaction(new SpeedReaction(1));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        fastButton.setDisable(true);
        okButton.setDisable(false);
        slowButton.setDisable(false);
    }

    /**
     * This method sends the corresponding SpeedReaction when the okButton is clicked.
     */
    public void fastEnough() {
        ReactionCommunication.postReaction(new SpeedReaction(0));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        fastButton.setDisable(false);
        okButton.setDisable(true);
        slowButton.setDisable(false);
    }

    /**
     * This method sends the corresponding SpeedReaction when the slowButton is clicked.
     */
    public void tooSlow() {
        ReactionCommunication.postReaction(new SpeedReaction(-1));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        fastButton.setDisable(false);
        okButton.setDisable(false);
        slowButton.setDisable(true);
    }

    /**
     * This method sends the corresponding EmotionReaction when the happyButton is clicked.
     */
    public void addHappy() {
        ReactionCommunication.postReaction(new EmotionReaction(1));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        happyButton.setDisable(true);
        sadButton.setDisable(false);
        confusedButton.setDisable(false);
    }

    /**
     * This method sends the corresponding EmotionReaction when the sadButton is clicked.
     */
    public void addSad() {
        ReactionCommunication.postReaction(new EmotionReaction(0));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        happyButton.setDisable(false);
        sadButton.setDisable(true);
        confusedButton.setDisable(false);
    }

    /**
     * This method sends the corresponding EmotionReaction when the confusedButton is clicked.
     */
    public void addConfused() {
        ReactionCommunication.postReaction(new EmotionReaction(-1));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        happyButton.setDisable(false);
        sadButton.setDisable(false);
        confusedButton.setDisable(true);
    }

    @Override
    public void closeWindow() {
        super.closeWindow();
        StudentFeedbackSceneController.init();
    }

    /**
     * for tests.
     */
    public void showFeedback() {
        StudentFeedbackSceneController.init();
    }

    /**
     * Method to load the poll scene.
     * @throws IOException if it cant load the fxml file
     */
    public void goToPolls() throws IOException {
        changeScene("/studentPollScene.fxml", 0.8);
    }

    public void goToHome() throws IOException {
        changeScene("/studentMainScene.fxml", 0.8);
    }

    public void goToLog() throws IOException {
        changeScene("/studentQuestionLogScene.fxml", 0.8);
    }

    /**
     * Every 2 seconds the client side of the app asks the server for a
     * questions update for this user. If there is one, this method
     * is called by the QuestionCommunication class.
     * @param result - depending on the update, the result can be -1 for
     *               a question discarded or 0 for question marked
     *               as answered. Depending on that a pop up appears and
     *               notifies the user for its question update.
     */
    public static void questionUpdatePopUp(QuestionsUpdate result) {

        //String[] updateInformation = result.split("/");
        String additionalText = "";

        String text = "";
        if (result.getStatusQuestion() == -1) {
            text = "Your question has been discarded!";
            additionalText = "Your question: \"" + result.getQuestionText()
                    + "\" has been discarded!";
        } else if (result.getStatusQuestion() == 0) {
            text = "Your question has been marked as answered!";
            additionalText = "Your question: \"" + result.getQuestionText()
                    + "\" has been marked as answered!";
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setWidth(900);
        alert.setHeight(300);
        alert.setTitle("Update on your question!");
        alert.setHeaderText(text);
        alert.setContentText(additionalText);
        alert.showAndWait();

    }

    @Override
    public void changeTheme(boolean mode) {

        List<VBox> menuList = new ArrayList<>();
        menuList.add(mainMenu);
        menuList.add(speedMenu);
        menuList.add(reactionMenu);

        if (mode) {
            colourChange(menuList, "lightMenuBackground", "darkMenuBackground",
                    "menuBtnBlack", "menuBtnDark",
                    "labelBlack", "labelDark");
        } else {
            colourChange(menuList, "darkMenuBackground", "lightMenuBackground",
                    "menuBtnDark", "menuBtnBlack",
                    "labelDark", "labelBlack");
        }

        super.changeTheme(mode);
    }

    /**
     * This method applies the colour changes.
     * @param menuList - the menu components.
     * @param removeMenu - the previous menu colour.
     * @param addMenu - the new menu colour.
     * @param removeButton - the previous button colour.
     * @param addButton - the current button colour.
     * @param removeLabel - the previous label colour.
     * @param addLabel - the current label colour.
     */
    public void colourChange(List<VBox> menuList, String removeMenu,
                             String addMenu, String removeButton,
                             String addButton, String removeLabel, String addLabel) {
        for (VBox box : menuList) {
            box.getStyleClass().removeAll(Collections.singleton(removeMenu));
            box.getStyleClass().add(addMenu);
            for (Node node : box.getChildren()) {
                if (node instanceof Button) {
                    node.getStyleClass().removeAll(Collections.singleton(removeButton));
                    if (!node.getStyleClass().contains("menuBtnWhite")) {
                        node.getStyleClass().add(addButton);
                    }
                } else {
                    node.getStyleClass().removeAll(Collections.singleton(removeLabel));
                    node.getStyleClass().add(addLabel);
                }
            }
        }
    }


}

