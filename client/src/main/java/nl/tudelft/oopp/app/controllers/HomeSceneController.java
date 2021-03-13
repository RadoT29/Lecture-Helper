package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.models.Entry;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

import java.io.IOException;
import java.util.PriorityQueue;

/**
 * This class contains the code that is run when the IO objects in the Home page are utilized.
 */
public class HomeSceneController {

    Session session = Session.getInstance();

    @FXML
    private TextField questionInput;

    @FXML
    private VBox questionBox;

    protected PriorityQueue<Entry> questions;

    /**
     * Pressing the sendButton will send all the text in the questionInput
     * to the sever as a Question object.
     */
    public void sendQuestion() {
        Question question = new Question(questionInput.getText());
        HomeSceneCommunication.postQuestion(question);
        questionInput.clear(); // clears question input box
    }

    /**
     * close the room.
     */
    public void closeRoom() {
        Session session = Session.getInstance();
        String linkId = session.getRoomLink();
        ServerCommunication.closeRoom(linkId);
    }

    /**
     * kick all students.
     */
    public void kickAllStudents() {
        Session session = Session.getInstance();
        String linkId = session.getRoomLink();
        ServerCommunication.kickAllStudents(linkId);
    }

    public void refresh() {
        questions = new PriorityQueue<>();
        questions.add(new Entry(new Question("Question 2"), 10));
        questions.add(new Entry(new Question("Question 3"), 11));
        questions.add(new Entry(new Question("Question 1"), 9));
//        questions.addAll(HomeSceneCommunication.getQuestions());
        loadQuestions();
    }

    /**
     * loads questions in the question box in the correct format.
     */
    public void loadQuestions() {

        String resource = "/questionCellStudent.fxml";
        if (session.getIsModerator()) {
            resource = "/questionCellModerator.fxml";
        }

        questionBox.getChildren().clear();
        int count = 1;
        while (!questions.isEmpty()) {
            Entry entry = questions.poll();
            try {
                questionBox.getChildren()
                        .add(createQuestionCell(entry, resource));
            } catch (IOException e) {
                questionBox.getChildren().add(
                        new Label("Something when wrong while loading this question"));
            }
        }
    }

    /**
     * creates a node for a question
     * @param entry Entry to be displayed
     * @param resource String the path to the resource with the question format
     * @return Node that is ready to be displayed
     * @throws IOException if the loader fails
     *      or one of the fields that should be changed where not found
     */
    protected Node createQuestionCell (Entry entry, String resource) throws IOException {
        Node newQuestion = FXMLLoader.load(getClass().getResource(resource));

        //set the node id to the question id
        newQuestion.setId(entry.getQuestion().getQuestionID());
        //set the question text
        Label l = (Label) newQuestion.lookup("#questionTextLabel");
        l.setText(entry.getQuestion().questionText);
        //set the upvote
        Label u = (Label) newQuestion.lookup(("#upvoteLabel"));
        u.setText("+" + entry.getUpvote());

        return newQuestion;
    }
}