package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.communication.QuestionCommunication;
import nl.tudelft.oopp.app.models.Answer;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;
import nl.tudelft.oopp.app.models.User;

import java.awt.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Locale;

public class QuestionCellController {

    @FXML
    Button dismissButton;

    @FXML
    Button upvoteButton;

    @FXML
    HBox questionCell;

    @FXML
    HBox questionLogCell;

    @FXML
    Label questionTextLabel;

    @FXML
    Label answerTextLabel;

    @FXML
    Button editButton;

    @FXML
    Button answerButton;

    @FXML
    Button answerButtonLog;


    private HomeSceneController hsc;
    private Question question;

    public void setHomeScene(HomeSceneController hsc) {
        this.hsc = hsc;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    /**
     * This method initializes a question cell.
     * @param question - a question.
     * @param resource - the fxml file.
     * @param hsc - the controller, from which the method is called.
     * @return - a visual representation of the question cell.
     * @throws IOException - may be thrown.
     */
    public static Node init(Question question, String resource, HomeSceneController hsc)
            throws IOException {
        FXMLLoader loader = new FXMLLoader(EditQuestionSceneController
                .class.getResource(resource));
        // load the question to a newNode and set it's homeSceneController to this
        Node newQuestion = loader.load();
        QuestionCellController qsc = loader.getController();
        qsc.setHomeScene(hsc);

        //set the node id to the question id
        newQuestion.setId(question.getId() + ""); //to deleted
        qsc.setQuestion(question);

        //set the question text
        Label questionLabel = (Label) newQuestion.lookup("#questionTextLabel");
        questionLabel.setText(question.questionText);

        Label nicknameLabel = (Label) newQuestion.lookup("#nickname");
        nicknameLabel.setText(question.user.getName());
        
        //set the upvote count
        Label upvoteLabel = (Label) newQuestion.lookup(("#upvoteLabel"));
        upvoteLabel.setText("+" + question.getUpVotes());

        return newQuestion;
    }

    /**
     * dismisses the question.
     * deletes the database from the database and remove it from the screen
     **/
    public void dismissClicked() {
        //get the id of the question to be deleted
        String id = question.getId() + "";
        Session session = Session.getInstance();

        if (session.getIsModerator()) {
            //delete the question from the database if moderator
            QuestionCommunication.dismissQuestion(Long.parseLong(id));

            //if statement checks if the question is in the list of questions
            //made in the user's session
        } else if (session.getQuestionsMade().contains(id)) {
            String user = Session.getInstance().getUserId();
            session.questionDeleted(id);
            QuestionCommunication.dismissSingular(Long.parseLong(id), Long.parseLong(user));
        }


        hsc.refresh();
    }

    /**
     * Delete a question. This is done from the Question log ScrollPane.
     */
    public void deleteFromLog() {

        Node question = questionLogCell.getParent();
        String id = question.getId();
        QuestionCommunication.dismissQuestion(Long.parseLong(id));
        //hsc.deleteQuestionFromScene(id);
    }

    /**
     * Method called when the upvote button is clicked,
     * it will first get the ID of the question in which the button was clicked.
     * Then the method will make a call to setUpvote so that the upvote is changed
     * (based on the users previous actions)
     */
    public void upvoteClicked() {
        //!
        Node question = questionCell.getParent();
        String id = question.getId();

        setUpvote(id);

        if (upvoteButton.getStyleClass().contains("active")) {
            upvoteButton.getStyleClass().remove("active");

        } else {
            upvoteButton.getStyleClass().add("active");

        }

        hsc.refresh();
    }

    /**
     * Method to make a change in the upvote status,
     * It will check if the question selected has been upVoted by the user
     * If so the upVote will be decremented, if not it will be incremented
     * (through methods established in Session).
     *
     * @param questionId - QuestionId where upvote is being added
     */
    public void setUpvote(String questionId) {
        Session session = Session.getInstance();

        boolean upvote = session.getUpVotedQuestions().contains(questionId);

        if (upvote) {
            session.decrementUpvotes(questionId);
            QuestionCommunication.deleteUpvote(questionId);
        } else {
            session.incrementUpvotes(questionId);
            QuestionCommunication.upVoteQuestion(questionId);
        }

    }

    /**
     * Method for blocking students by IP.
     */
    public void blockUser() {
        //!
        Node question = questionCell.getParent();
        //User user = (User) question.getUserData();
        System.out.println(question.getId());
        Session session = Session.getInstance();
        HomeSceneCommunication.banUserForThatRoom(question.getId(), session.getRoomLink());
    }


    /**
     * Handles click in the edit button.
     * Tries to load new edit question scene.
     */
    public void editClicked() {

        //get question id
        String oldText = questionTextLabel.getText();
        String id = question.getId() + "";

        //load edit question scene
        try {
            EditQuestionSceneController.init(oldText, id, this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create an answer from the log scene.
     */
    public void answerFromLog() {
        String oldAnswer = answerTextLabel.getText();
        Node question = questionLogCell.getParent();
        String id = question.getId();
        Session session = Session.getInstance();

        String userId = session.getUserId();

        try {
            AnswerSceneController.initialize(oldAnswer, id, userId);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method called when the upvote button is clicked,
     * it will first get the ID of the question in which the button was clicked.
     * Then the method will make a call to setUpvote so that the upvote is changed
     * (based on the users previous actions)
     */
    public void answeredClicked() {
        //!
        Node question = questionCell.getParent();
        String id = question.getId();

        setAnswer(id);
        answerButton.getStyleClass().add("active");
        hsc.refresh();
    }



    /**
     * Method to set a question as answered,
     * It will check if the question selected has been marked as answered
     * If not a new answer will be created
     * (through methods established in communication).
     * @param questionId - QuestionId where answer is being added
     */
    public void setAnswer(String questionId) {
        Session session = Session.getInstance();

        boolean status = QuestionCommunication.checkAnswered(questionId);

        if (!status) {
            QuestionCommunication.setAnswered(questionId, true);
        } else {
            System.out.println("This question was already answered");
        }

    }



    public void refresh() {
        hsc.refresh();
    }
}
