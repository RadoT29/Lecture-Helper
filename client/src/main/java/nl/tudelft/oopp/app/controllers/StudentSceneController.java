package nl.tudelft.oopp.app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import nl.tudelft.oopp.app.communication.ReactionCommunication;
import nl.tudelft.oopp.app.models.EmotionReaction;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.SpeedReaction;

/**
 * This class controls the Main scene of the Students.
 */
public class StudentSceneController extends HomeSceneController implements Initializable {

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
    private TranslateTransition closeSpeedFastNav;

    private TranslateTransition openReactionNav;
    private TranslateTransition closeReactionNav;
    private TranslateTransition closeReactionFastNav;

    /**
     * This method initializes the state of the navigation bar.
     * It hides the sliding bars behind the regular one.
     * @param url - The path.
     * @param rb - Provides any needed resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        openSpeedNav = new TranslateTransition(Duration.millis(100), speedMenu);
        openSpeedNav.setToX(speedMenu.getTranslateX() - speedMenu.getWidth());
        closeSpeedNav = new TranslateTransition(Duration.millis(100), speedMenu);
        closeSpeedFastNav = new TranslateTransition(Duration.millis(.1), speedMenu);

        openReactionNav = new TranslateTransition(Duration.millis(100), reactionMenu);
        openReactionNav.setToX(reactionMenu.getTranslateX() - reactionMenu.getWidth());
        closeReactionNav = new TranslateTransition(Duration.millis(100), reactionMenu);
        closeReactionFastNav = new TranslateTransition(Duration.millis(.1), reactionMenu);

        mainBoxLog.setVisible(false);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                closeSpeedFastNav.setToX(-(speedMenu.getWidth()));
                closeSpeedFastNav.play();
                closeReactionFastNav.setToX(-(reactionMenu.getWidth()));
                closeReactionFastNav.play();
            }
        });

        super.initialize(url,rb);
    }

    /**
     * This method closes the sliding part of the speed bar.
     */
    public void hideSpeedBar() {
        speedButton.setStyle("-fx-background-color:" + buttonColour);
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
            speedButton.setStyle("-fx-background-color: white");
            openSpeedNav.play();
        } else {
            hideSpeedBar();
        }
    }

    /**
     * This method closes the sliding part of the reaction bar.
     */
    public void hideReactionBar() {
        reactionButton.setStyle("-fx-background-color:" + buttonColour);
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
            reactionButton.setStyle("-fx-background-color: white");
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


    /**
     *  Creates a node for a question in the question log scene.
     * @param question - the question.
     * @param resource - the question cell.
     * @return - a Node of the question.
     */
    @Override
    protected Node createQuestionCellLog(Question question, String resource) throws IOException {

        Node newQuestion = super.createQuestionCellLog(question, resource);

        HBox buttonBox = (HBox) newQuestion.lookup("#buttonBox");
        buttonBox.setVisible(false);

        return newQuestion;
    }

    @Override
    public void changeTheme(
            boolean mode, String buttonColour, String menuColour, String textColour,
            String inputColour, String backgroundColour) {
        ArrayList<VBox> list = new ArrayList<>();
        list.add(reactionMenu);
        list.add(speedMenu);
        list.add(mainMenu);

        for (VBox box : list) {
            box.setStyle("-fx-background-color:" + menuColour);
            for (Node node : box.getChildren()) {
                if (node instanceof Button) {
                    node.setStyle("-fx-background-color:" + buttonColour);
                } else {
                    node.setStyle("-fx-text-fill:" + textColour);
                }
            }
        }
        super.changeTheme(mode, buttonColour, menuColour,
                textColour, inputColour, backgroundColour);
    }
}

