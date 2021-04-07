package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import nl.tudelft.oopp.app.communication.BanCommunication;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.communication.QuestionCommunication;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.OutOfLimitOfQuestionsException;
import nl.tudelft.oopp.app.exceptions.UserWarnedException;
import nl.tudelft.oopp.app.models.Question;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class StudentQuestionSceneController extends StudentSceneController {

    @FXML
    private Label passLimitQuestionsLabel;
    @FXML
    protected VBox questionBox;
    @FXML
    private TextField questionInput;
    @FXML
    private HBox textBox;
    @FXML
    private Label roomName;
    @FXML
    private Button sendButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
    }

    /**
     * This method is constantly called by a thread and refreshes the page.
     *
     * @throws ExecutionException           - may be thrown.
     * @throws InterruptedException         - may be thrown.
     * @throws NoStudentPermissionException - may be thrown.
     * @throws AccessDeniedException        - may be thrown.
     * @throws UserWarnedException          - may be thrown.
     */
    public void constantRefresh() throws ExecutionException, InterruptedException,
            NoStudentPermissionException, AccessDeniedException, UserWarnedException {
        questions = new PriorityQueue<>();
        questions.addAll(HomeSceneCommunication.constantlyGetQuestions(session.getRoomLink()));
        loadQuestions();


        //ServerCommunication.isTheRoomClosed(session.getRoomLink());

        //ServerCommunication.hasStudentPermission(session.getRoomLink());
        ServerCommunication.isRoomOpenStudents(session.getRoomLink());
        QuestionCommunication.updatesOnQuestions(session.getUserId(), session.getRoomLink());
        if (!session.isWarned()) {
            BanCommunication.isIpWarned(session.getRoomLink());
        } else {
            BanCommunication.isIpBanned(session.getRoomLink());
        }

    }

    /**
     * Method to refresh.
     */
    public void refresh() {
        questions = new PriorityQueue<>();
        questions.addAll(HomeSceneCommunication.getQuestions());
        loadQuestions();
    }

    /**
     * Pressing the sendButton will send all the text in the questionInput
     * to the sever as a Question object.
     * The question is also added to the list of questions made by the session's user
     * (each client will have these stored locally)
     */
    public void sendQuestion() {

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
     * loads questions in the question box in the correct format.
     */
    public void loadQuestions() {

        String resource = "/questionCellStudent.fxml";

        questionBox.getChildren().clear();
        while (!questions.isEmpty()) {
            Question question = questions.poll();
            try {
                questionBox.getChildren()
                        .add(QuestionCellController.init(question, resource, this));
            } catch (IOException e) {
                questionBox.getChildren().add(
                        new Label("Something went wrong while loading this question"));
            }
        }
    }

    @Override
    public void changeTheme(boolean mode) {
        String boxColourAdd;
        String boxColourRemove;
        String inputColourAdd;
        String inputColourRemove;
        String addLabel;
        String removeLabel;
        String buttonAdd;
        String buttonRemove;

        if (mode) {
            boxColourAdd = "borderWhite";
            boxColourRemove = "borderBlack";
            inputColourAdd = "labelWhite";
            inputColourRemove = "labelBlack";
            addLabel = "labelDark";
            removeLabel = "labelBlack";
            buttonAdd = "menuBtnWhite";
            buttonRemove = "menuBtnBlack";
        } else {
            boxColourAdd = "borderBlack";
            boxColourRemove = "borderWhite";
            inputColourAdd = "labelBlack";
            inputColourRemove = "labelWhite";
            addLabel = "labelBlack";
            removeLabel = "labelDark";
            buttonAdd = "menuBtnBlack";
            buttonRemove = "menuBtnWhite";
        }
        applyColour(boxColourAdd, boxColourRemove, inputColourAdd, inputColourRemove,
                addLabel, removeLabel, buttonAdd, buttonRemove);
        super.changeTheme(mode);
    }

    private void applyColour(String boxColourAdd, String boxColourRemove, String inputColourAdd,
                             String inputColourRemove, String addLabel, String removeLabel,
                             String buttonAdd, String buttonRemove) {

        textBox.getStyleClass().removeAll(Collections.singleton(boxColourRemove));
        textBox.getStyleClass().add(boxColourAdd);
        questionInput.getStyleClass().removeAll(Collections.singleton(inputColourRemove));
        questionInput.getStyleClass().add(inputColourAdd);
        roomName.getStyleClass().removeAll(Collections.singleton(removeLabel));
        roomName.getStyleClass().add(addLabel);
        sendButton.getStyleClass().removeAll(Collections.singleton(buttonRemove));
        sendButton.getStyleClass().add(buttonAdd);
    }
}
