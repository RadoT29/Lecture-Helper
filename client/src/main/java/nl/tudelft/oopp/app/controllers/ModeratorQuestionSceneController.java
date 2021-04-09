package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.app.communication.*;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.UserWarnedException;
import nl.tudelft.oopp.app.models.Question;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class ModeratorQuestionSceneController extends ModeratorSceneController {


    @FXML
    protected VBox questionBox;
    @FXML
    public HBox reactionBox;
    @FXML
    public Button speedStat;
    @FXML
    public HBox emotionReactions;

    ModeratorReactionController reactionController;

    /**
     * This method initializes the state of the navigation bar.
     * It hides the sliding bar behind the regular one.
     * It also refreshes the scene.
     * @param url - The path.
     * @param rb - Provides any needed resources.
     */
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        reactionController = new ModeratorReactionController(emotionReactions);
        refresh();
    }

    /**
     * This method changes the icons that represent the Speed and Emotion Reaction to
     * match their statistics.
     */
    public void loadStats() {
        int speedStatInt = ReactionCommunication.getReactionStats(true);


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
        questions = new PriorityQueue<>();
        questions.addAll(HomeSceneCommunication.getQuestions());
        loadQuestions();
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

    /**
     * loads questions in the question box in the correct format.
     */
    public void loadQuestions() {

        String resource = "/questionCellModerator.fxml";

        questionBox.getChildren().clear();
        while (!questions.isEmpty()) {
            Question question = questions.poll();
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
     * Pressing the sendButton will send all the text in the questionInput
     * to the sever as a Question object.
     * The question is also added to the list of questions made by the session's user
     * (each client will have these stored locally)
     */
    public void sendQuestion() {

        // If input is null, don't send question
        if (questionInput.getText().equals("")) {
            return;
        }

        Question question = new Question(questionInput.getText());
        HomeSceneCommunication.postQuestion(question);

        //Sends a request to get the questionId of the question just created
        Long questionId = HomeSceneCommunication.getSingleQuestion();
        //Adds said question to the users list of created questions
        session.questionAdded(String.valueOf(questionId));

        questionInput.clear(); // clears question input box
        refresh();
    }

    /**
     * handles click on moreReactionButton.
     * shows/hides the emotion reactions on the screen
     * and changes the button to + or -
     */
    public void moreReactionsClicked() {
        if (!emotionReactions.isVisible()) {
            //display emotion reactions
            moreReactionButton.getStyleClass().set(1, "hideButton");
            reactionController.showEmotion();
        } else {
            //hide emotion reactions
            moreReactionButton.getStyleClass().set(1, "expandButton");
            reactionController.hideEmotion();
        }

    }

    /**
     * shows feedback from students in the scene.
     */
    public void showFeedback() {
        try {
            ViewFeedbackSceneController.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeTheme(boolean mode) {
        changeColourMainScene(mode);

        String buttonRemove;
        String buttonAdd;
        String labelAdd;
        String labelRemove;

        List<HBox> list = new ArrayList<>();
        list.add(reactionBox);
        list.add(emotionReactions);

        if (mode) {
            buttonAdd = "menuBtnDark";
            buttonRemove = "menuBtnBlack";
            labelAdd = "labelDark";
            labelRemove = "labelBlack";
        } else  {
            buttonAdd = "menuBtnBlack";
            buttonRemove = "menuBtnDark";
            labelAdd = "labelBlack";
            labelRemove = "labelDark";
        }
        changeReactionColour(list, buttonAdd, buttonRemove,
                labelAdd, labelRemove);

        super.changeTheme(mode);
    }

    /**
     * Applies the colour changes to the reaction icons.
     * @param list - teh parents of the buttons.
     * @param buttonAdd - the colour of the icon.
     * @param buttonRemove - the previous colour of the icon.
     * @param labelAdd - the label of the score.
     * @param labelRemove - the previous label.
     */
    private void changeReactionColour(List<HBox> list, String buttonAdd,
                                       String buttonRemove, String labelAdd,
                                       String labelRemove) {
        for (HBox box : list) {
            for (Node child : box.getChildren()) {
                if (child instanceof Button) {
                    child.getStyleClass().removeAll(Collections.singleton(buttonRemove));
                    child.getStyleClass().add(buttonAdd);
                } else {
                    child.getStyleClass().removeAll(Collections.singleton(labelRemove));
                    child.getStyleClass().add(labelAdd);
                }
            }
        }
    }
}
