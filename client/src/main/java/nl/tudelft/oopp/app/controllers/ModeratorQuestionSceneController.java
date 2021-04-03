package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import nl.tudelft.oopp.app.communication.ReactionCommunication;

public class ModeratorQuestionSceneController extends ModeratorSceneController {


    @FXML
    public HBox reactionBox;
    @FXML
    public Button speedStat;
    @FXML
    public HBox emotionReactions;


    /**
     * This method changes the icons that represent the Speed and Emotion Reaction to
     * match their statistics.
     */
    public void loadStats() {
        int speedStatInt = ReactionCommunication.getReactionStats(true);

        System.out.println("speed = " + speedStatInt);

        if (speedStatInt == 1) {
            speedStat.getStyleClass().set(1,"fastButton");
        } else if (speedStatInt == -1) {
            speedStat.getStyleClass().set(1,"slowButton");
        } else {
            speedStat.getStyleClass().set(1,"okButton");
        }

    }

    /**
     * Fill in the priority queue and and load them on the screen.
     */
    public void refresh() {
        loadStats();
        reactionController.update();
    }
}
