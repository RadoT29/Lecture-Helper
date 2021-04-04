package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.models.Question;

import java.awt.*;
import java.io.IOException;
import java.util.PriorityQueue;

public class ModeratorLogSceneController extends ModeratorSceneController {

    @FXML
    protected VBox questionBoxLog;

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
     * Loads all answered questions in the question log.
     */
    public void loadAnsweredQuestions() {

        questionBoxLog.getChildren().clear();
        while (!questions.isEmpty()) {
            Question question = questions.poll();
            try {
                questionBoxLog.getChildren()
                        .add(createQuestionCellLog(question));
            } catch (IOException e) {
                questionBoxLog.getChildren().add(
                        new Label("Something went wrong while loading this question"));
            }
        }
    }

    /**
     * Creates a node for a question in the question log scene.
     * @param question - the question.
     * @return - a Node of the question.
     */
    protected Node createQuestionCellLog(Question question) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/questionCellQuestionLog.fxml"));
        Node newQuestion = loader.load();
        QuestionCellController qsc = loader.getController();
        qsc.setHomeScene(this);

        //set the node id to the question id
        newQuestion.setId(question.getId() + "");
        Label questionLabel = (Label) newQuestion.lookup("#questionTextLabelLog");
        questionLabel.setText(question.questionText);
        Label answerLabel = (Label) newQuestion.lookup("#answerTextLabel");
        answerLabel.setText(question.answerText);

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.4;
        questionLabel.setPrefWidth(width);
        questionLabel.setMaxWidth(width);

        answerLabel = (Label) newQuestion.lookup("#answerTextLabel");

        if (!answerLabel.getText().equals("This question was answered during the lecture")) {
            javafx.scene.control.Button answerButtonLog =
                    (Button) newQuestion.lookup("#answerButtonLog");
            answerButtonLog.getStyleClass().remove("answerButton");
            answerButtonLog.getStyleClass().add("editButton");
            answerButtonLog.setText("Edit");
        }
        return newQuestion;
    }
}