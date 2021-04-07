package nl.tudelft.oopp.app.controllers;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.models.Question;
import java.io.IOException;
import java.net.URL;
import java.util.PriorityQueue;
import java.util.ResourceBundle;

public class StudentLogSceneController extends StudentSceneController {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
    }

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
        HBox buttonBox = (HBox) newQuestion.lookup("#buttonBox");
        buttonBox.setVisible(false);

        return newQuestion;
    }

    @Override
    public void refresh() {

    }

    @Override
    public void changeTheme(boolean mode) {
        changeColourQuestionLog(mode);
        super.changeTheme(mode);
    }
}
