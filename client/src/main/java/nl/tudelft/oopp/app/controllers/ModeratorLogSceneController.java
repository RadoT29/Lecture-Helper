package nl.tudelft.oopp.app.controllers;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.models.Question;
import java.io.IOException;
import java.util.PriorityQueue;

public class ModeratorLogSceneController extends ModeratorSceneController {

    /**
     * This method is constantly called by a thread and refreshes the page.
     * @throws InterruptedException - Thrown when a thread is waiting, sleeping,
     *                                or otherwise occupied, and the thread is interrupted,
     *                                either before or during the activity.
     */
    public void constantRefresh() throws InterruptedException {

        questions = new PriorityQueue<>();
        questions.addAll(HomeSceneCommunication
                .constantlyGetAnsweredQuestions(session.getRoomLink()));
        loadAnsweredQuestions();
    }

    public void refresh() {

    }

    /**
     * Creates a node for a question in the question log scene.
     * @param question - the question.
     * @return - a Node of the question.
     */
    @Override
    protected Node createQuestionCellLog(Question question) throws IOException {

        Node newQuestion = super.createQuestionCellLog(question);

        Label answerLabel = (Label) newQuestion.lookup("#answerTextLabel");

        if (!answerLabel.getText().equals("This question was answered during the lecture")) {
            javafx.scene.control.Button answerButtonLog =
                    (Button) newQuestion.lookup("#answerButtonLog");
            answerButtonLog.getStyleClass().remove("answerButton");
            answerButtonLog.getStyleClass().add("editButton");
            answerButtonLog.setText("Edit");
        }
        return newQuestion;
    }

    @Override
    public void changeTheme(boolean mode) {
        changeColourQuestionLog(mode);
        super.changeTheme(mode);
    }
}