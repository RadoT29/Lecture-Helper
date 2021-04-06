package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.models.Question;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.ResourceBundle;

public class StudentLogSceneController extends StudentSceneController {

    @FXML
    public Label logLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        changeTheme(darkTheme);
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
        String addLabel;
        String removeLabel;

        if (mode) {
            addLabel = "labelDark";
            removeLabel = "labelBlack";
        } else {
            addLabel = "labelBlack";
            removeLabel = "labelDark";
        }

        logLabel.getStyleClass().removeAll(Collections.singleton(removeLabel));
        logLabel.getStyleClass().add(addLabel);
        super.changeTheme(mode);
    }
}
