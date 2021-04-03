package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import nl.tudelft.oopp.app.communication.*;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.UserWarnedException;

import java.util.PriorityQueue;
import java.util.concurrent.ExecutionException;

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
            speedStat.getStyleClass().set(1, "fastButton");
        } else if (speedStatInt == -1) {
            speedStat.getStyleClass().set(1, "slowButton");
        } else {
            speedStat.getStyleClass().set(1, "okButton");
        }

    }

    /**
     * Fill in the priority queue and and load them on the screen.
     */
    public void refresh() {
        loadStats();
        reactionController.update();
    }

    /**
     * This method is constantly called by a thread and refreshes the page.
     *
     * @throws ExecutionException           - may be thrown.
     * @throws InterruptedException         - may be thrown.
     * @throws NoStudentPermissionException - may be thrown.
     * @throws AccessDeniedException        - may be thrown.
     * @throws UserWarnedException          - may be thrown.
     */
    public void constantRefresh() throws ExecutionException, InterruptedException,
            NoStudentPermissionException, AccessDeniedException, UserWarnedException {
        questions = new PriorityQueue<>();
        questions.addAll(HomeSceneCommunication.constantlyGetQuestions(session.getRoomLink()));
        loadQuestions();

        String linkId = session.getRoomLink();
        try {
            ServerCommunication.isRoomOpenStudents(linkId);
            changeImageOpenRoomButton();
        } catch (NoStudentPermissionException exception) {
            changeImageCloseRoomButton();
        }

    }
}
