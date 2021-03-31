package nl.tudelft.oopp.app.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.communication.QuestionCommunication;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.communication.SplashCommunication;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.OutOfLimitOfQuestionsException;
import nl.tudelft.oopp.app.exceptions.RoomIsClosedException;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.PriorityQueue;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class contains the code that is run when the IO objects in the Home page are utilized.
 */
public class HomeSceneController {

    private boolean interruptThread = false;
    private boolean openOne = true;

    Session session = Session.getInstance();

    @FXML
    private TextField questionInput;

    @FXML
    private VBox questionBox;

    @FXML
    private Label passLimitQuestionsLabel;

    protected PriorityQueue<Question> questions;


    /**
     * This method initializes the thread,
     * which is responsible for constantly refreshing the questions.
     *
     * @param url - The path.
     * @param rb  - Provides any needed resources.
     */
    public void initialize(URL url, ResourceBundle rb) {

        // This thread will periodically refresh the content of the question queue.

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!(Thread.interrupted())) {
                    try {
                        Platform.runLater(() -> {
                            try {
                                constantRefresh();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                closeWindow();
                            }
                        });

                        Thread.sleep(2000);
                        if (interruptThread) {
                            Thread.currentThread().interrupt();
                            break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    /**
     * When this method is called it:
     * 1. set the boolean variable interruptThread = true
     * which afterwards interrupts the thread
     * 2. Open the Splash Scene and should close the current one
     */

    public void closeWindow() {
        interruptThread = true;
        if (!openOne) {
            return;
        }
        Parent loader = null;
        try {
            loader = new FXMLLoader(getClass().getResource("/splashScene.fxml")).load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage linkStage = (Stage) questionInput.getScene().getWindow();
        //Scene scene = new Scene(loader);

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.8;
        double height = screenSize.getHeight() * 0.8;

        Scene scene = new Scene(loader, width, height);

        linkStage.setScene(scene);
        linkStage.centerOnScreen();
        linkStage.show();
        openOne = false;

    }

    /**
     * Pressing the sendButton will send all the text in the questionInput
     * to the sever as a Question object.
     * The question is also added to the list of questions made by the session's user
     * (each client will have these stored locally)
     */
    public void sendQuestion() {
        if (!session.getIsModerator()) {
            passLimitQuestionsLabel.setVisible(false);
            try {
                HomeSceneCommunication.isInLimitOfQuestion(session.getUserId(),
                                                        session.getRoomLink());
            } catch (OutOfLimitOfQuestionsException exception) {
                System.out.println("Out of limit");
                passLimitQuestionsLabel.setVisible(true);
                //passLimitQuestionsLabel.wait(4000);
                questionInput.clear();
                return;
            }
        }
        System.out.println("in limit");
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
     * This method is constantly called by a thread and refreshes the page.
     *
     * @throws ExecutionException           - may be thrown.
     * @throws InterruptedException         - may be thrown.
     * @throws NoStudentPermissionException - may be thrown.
     * @throws RoomIsClosedException        - may be thrown.
     * @throws AccessDeniedException        - may be thrown.
     */
    public void constantRefresh() throws ExecutionException, InterruptedException,
            NoStudentPermissionException, RoomIsClosedException, AccessDeniedException {
        questions = new PriorityQueue<>();
        
        //used to update the upvotecount locally considering moderator upVotes
        List<Question> unsortedQuestions =
                HomeSceneCommunication.constantlyGetQuestions(session.getRoomLink());
        for (Question q: unsortedQuestions) {
            q.setUpVotesFinal(getTotalUpVotes(q));
        }
        
        questions.addAll(unsortedQuestions);
        loadQuestions();
        if (!session.getIsModerator()) {
            ServerCommunication.hasStudentPermission(session.getRoomLink());
        }

        ServerCommunication.isTheRoomClosed(session.getRoomLink());
        if (!session.getIsModerator()) {
            SplashCommunication.isIpBanned(session.getRoomLink());
        }


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
        newQuestion.setId(question.getId() + "");

        //Check if the question loaded was created by the session's user
        checkForQuestion(newQuestion, question);

        //set the question text
        Label questionLabel = (Label) newQuestion.lookup("#questionTextLabel");
        questionLabel.setText(question.questionText);

        Label nicknameLabel = (Label) newQuestion.lookup("#nickname");
        nicknameLabel.setText(question.user.getName());

        //set the question box size
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.4;
        questionLabel.setPrefWidth(width);
        questionLabel.setMaxWidth(width);

        //set the upvote count
        Label upvoteLabel = (Label) newQuestion.lookup(("#upvoteLabel"));
        upvoteLabel.setText("+" + getTotalUpVotes(question));

        //set upvote button as active or inactive
        Button upvoteButton = (Button) newQuestion.lookup(("#upvoteButton"));
        boolean isActive = session.getUpVotedQuestions()
                .contains(String.valueOf(question.getId()));
        if (isActive) {
            upvoteButton.getStyleClass().add("active");
        }


        return newQuestion;
    }

    /**
     * finds the question by the id and deletes it from the scene.
     * (this method will probably be deleted once we implement the real-time updates)
     *
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
     * @param question    - the object of the new question created
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


    /**
     * Get the Date in which the room was created.
     * @return Date - at which room was created
     */
    public Date retrieveRoomTime() {
        Date roomDate = HomeSceneCommunication.getRoomTime().get(0);
        return roomDate;
    }

    /**
     * Get the Date in which the room was last modified.
     * @return Date - at which room was last modified
     */
    public Date retrieveModifiedTime() {
        Date roomDate = HomeSceneCommunication.getRoomTime().get(1);
        return roomDate;
    }


    /**
     * Method to get the moderator upVotes with extra value.
     * @param question - question to retrieve upVotes from
     * @return number of upVotes
     */
    public int getTotalUpVotes(Question question) {
        int modUpVotes = QuestionCommunication.getModUpVotes(question.getId());

        int total = question.getUpVotes() + 9 * modUpVotes;
        question.setUpVotesFinal(total);
        
        return total;
    }


}