package nl.tudelft.oopp.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import nl.tudelft.oopp.app.communication.BanCommunication;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.communication.QuestionCommunication;
import nl.tudelft.oopp.app.exceptions.UserWarnedException;
import nl.tudelft.oopp.app.models.Answer;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;
import nl.tudelft.oopp.app.models.User;

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
    Label questionTextLabel;

    @FXML
    Button editButton;

    @FXML
    Button answerButton;

    String questionId;

    Session session = Session.getInstance();

    private HomeSceneController hsc;

    public void setHomeScene(HomeSceneController hsc) {
        this.hsc = hsc;
    }

    /**
     * dismisses the question.
     * deletes the database from the database and remove it from the screen
     **/
    public void dismissClicked() {
        //get the id of the question to be deleted
        Node question = questionCell.getParent();
        String id = question.getId();
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

        //remove the database from the screen
        hsc.deleteQuestionFromScene(id);

        hsc.refresh();
    }

    /**
     * Method called when the upvote button is clicked,
     * it will first get the ID of the question in which the button was clicked.
     * Then the method will make a call to setUpvote so that the upvote is changed
     * (based on the users previous actions)
     */
    public void upvoteClicked() {
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
    public void blockWarnUser() throws IOException {
        Node question = questionCell.getParent();
        questionId = question.getId();
        System.out.println("Question Id: "+questionId);
        Session session = Session.getInstance();
        try {
            BanCommunication.isIpWarned(session.getRoomLink());
        } catch (UserWarnedException e) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/banUserScene.fxml"));
            Stage linkStage = new Stage();
            Scene scene = new Scene(loader.load());

            //FXMLLoader banLoader =loader;
            BanUserController banUserController = loader.getController();
            banUserController.initData(questionId);

            linkStage.setScene(scene);
            linkStage.show();
            return;
        }

        BanCommunication.warnUserForThatRoom(question.getId(), session.getRoomLink());

    }


    /**
     * Handles click in the edit button.
     * Tries to load new edit question scene.
     */
    public void editClicked() {

        //get question id
        String oldText = questionTextLabel.getText();
        Node question = questionCell.getParent();
        String id = question.getId();

        //load edit question scene
        try {
            EditQuestionSceneController.init(oldText, id, this);

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
     *
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
