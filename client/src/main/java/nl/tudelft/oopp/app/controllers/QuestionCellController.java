package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import nl.tudelft.oopp.app.communication.QuestionCommunication;
import nl.tudelft.oopp.app.models.Session;

public class QuestionCellController {

    @FXML
    Button dismissButton;

    @FXML
    Button upvoteButton;

    @FXML
    HBox questionCell;


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
            System.out.println();
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
     * @param questionId - QuestionId where upvote is being added
     */
    public void setUpvote(String questionId) {
        Session session = Session.getInstance();

        boolean upvote = session.getUpvotesList().contains(questionId);

        if (upvote) {
            session.decrementUpvotes(questionId);
            QuestionCommunication.deleteUpvote(questionId);
        } else {
            session.incrementUpvotes(questionId);
            QuestionCommunication.upVoteQuestion(questionId);
        }

    }


}
