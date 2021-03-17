package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

import java.awt.*;
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

    protected PriorityQueue<Question> questions;

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

    /**
     * fill in the priority queue and and load them on the screen.
     */
    public void refresh() {
        questions = new PriorityQueue<>();
        questions.addAll(HomeSceneCommunication.getQuestions());
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
            Question question = questions.poll();
            try {
                questionBox.getChildren()
                        .add(createQuestionCell(question, resource));
            } catch (IOException e) {
                questionBox.getChildren().add(
                        new Label("Something went wrong while loading this question"));
            }
        }
    }

    /**
     * creates a node for a question.
     * @param resource String the path to the resource with the question format
     * @return Node that is ready to be displayed
     * @throws IOException if the loader fails
     *      or one of the fields that should be changed where not found
     */
    protected Node createQuestionCell(Question question, String resource) throws IOException {
        // load the question to a newNode and set it's homeSceneController to this
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Node newQuestion = loader.load();
        QuestionCellController qsc = loader.getController();
        qsc.setHomeScene(this);

        //set the node id to the question id
        newQuestion.setId(question.getQuestionID() + "");

        //Check if the question loaded was created by the session's user
        checkForQuestion(newQuestion, question);

        //set the question text
        Label questionLabel = (Label) newQuestion.lookup("#questionTextLabel");
        questionLabel.setText(question.questionText);

        //set the question box size
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.4;
        questionLabel.setPrefWidth(width);
        questionLabel.setMaxWidth(width);

        //set the upvote count
        Label upvoteLabel = (Label) newQuestion.lookup(("#upvoteLabel"));
        upvoteLabel.setText("+" + question.getUpVotes());

        //set upvote button as active or inactive
        Button upvoteButton = (Button) newQuestion.lookup(("#upvoteButton"));
        boolean isActive = session.getUpvotesList()
                .contains(String.valueOf(question.getQuestionID()));
        if (isActive) {
            upvoteButton.getStyleClass().add("active");
        }


        return newQuestion;
    }

    /**
    * finds the question by the id and deletes it from the scene.
    * (this method will probably be deleted once we implement the real-time updates)
    * @param id the id of the question to be deleted
    **/
    public void deleteQuestionFromScene(String id) {
        Node q = questionBox.lookup("#" + id);
        questionBox.getChildren().remove(q);
    }

    /**
     * Method to check whether the Question that is being created has been
     * written by the student in the session (so that the dismiss button
     * will be shown - if it does not correspond it won't show).
     * @param newQuestion - Node of the new question created
     * @param question - the object of the new question created
     */
    public void checkForQuestion(Node newQuestion, Question question) {
        if (!session.getIsModerator()) {
            Button d = (Button) newQuestion.lookup("#dismissButton");

            String id = String.valueOf(question.id);
            //Checks if the user made the question
            boolean createdQuestion = session.getQuestionsMade().contains(id);

            //If the question was created by user we want disabled to be set as false
            d.setDisable(!createdQuestion);

            //makes button visible
            if (createdQuestion) {
                d.setOpacity(1.0);
            }

        }
    }



}