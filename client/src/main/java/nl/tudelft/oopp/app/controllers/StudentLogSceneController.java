package nl.tudelft.oopp.app.controllers;

import javafx.scene.Node;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.models.Question;
import java.io.IOException;
import java.util.PriorityQueue;

public class StudentLogSceneController extends StudentSceneController {

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

    /**
     * Creates a node for a question in the question log scene.
     * @param question - the question.
     * @return - a Node of the question.
     */
    @Override
    protected Node createQuestionCellLog(Question question) throws IOException {

        Node newQuestion = super.createQuestionCellLog(question);

        return newQuestion;
    }

    @Override
    public void refresh() {

    }
}
