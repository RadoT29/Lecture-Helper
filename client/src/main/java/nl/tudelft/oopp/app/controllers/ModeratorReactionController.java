package nl.tudelft.oopp.app.controllers;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import nl.tudelft.oopp.app.communication.ReactionCommunication;

import java.util.List;

public class ModeratorReactionController {

    private HBox emotionReactions;

    /**
     * constructor for ModeratorReactionController.
     * @param emotionReaction - HBox with the emotion reactions
     */
    public ModeratorReactionController(HBox emotionReaction) {
        this.emotionReactions = emotionReaction;
    }


    /**
     * tries to show the emotion reaction counts.
     */
    public void showEmotion() {
        emotionReactions.setVisible(true);
        update();

    }

    /**
     * hides the emotion reactions.
     */
    public void hideEmotion() {
        emotionReactions.setVisible(false);
    }

    /**
     * gets a list of counts (index 0 - confused, 1 - sad, 2 - happy)
     * and displays them on the screen.
     * if something goes wrong displays a message.
     */
    public void update() {
        try {
            List<Integer> reactionCounts = ReactionCommunication.getAllReactionCount();
            ((Label) emotionReactions.lookup("#confusedCount")).setText(reactionCounts.get(0) + "");
            ((Label) emotionReactions.lookup("#sadCount")).setText(reactionCounts.get(1) + "");
            ((Label) emotionReactions.lookup("#happyCount")).setText(reactionCounts.get(2) + "");
        } catch (Exception e) {
            emotionReactions.getChildren().add(new Label("Something went wrong."));
        }
    }



}
