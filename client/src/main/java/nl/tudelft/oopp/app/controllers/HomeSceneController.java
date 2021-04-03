package nl.tudelft.oopp.app.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.communication.*;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.OutOfLimitOfQuestionsException;
import nl.tudelft.oopp.app.exceptions.UserWarnedException;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.QuestionsUpdate;
import nl.tudelft.oopp.app.models.Session;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.PriorityQueue;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

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
    private Button sendButton;
    @FXML
    private HBox textBox;

    @FXML
    protected VBox questionBox;

    @FXML
    protected VBox questionBoxLog;

    @FXML
    public Button questionButton;
    @FXML
    public AnchorPane mainBox;
    @FXML
    public AnchorPane mainBoxLog;

    @FXML
    public AnchorPane pane;

    @FXML
    public Button settingsButton;

    @FXML
    public Label logLabel;

    @FXML
    public Label settingsLabel;

    @FXML
    public Label roomName;

    @FXML
    private Label passLimitQuestionsLabel;

    @FXML
    private Button closeOpenRoomButton;

    @FXML
    private Label closeOpenRoomLabel;

    protected PriorityQueue<Question> questions;

    protected boolean keepRequesting;

    protected boolean keepRequestingLog;

    protected boolean darkTheme;




    /**
     * This thread will periodically refresh the content of the question queue.
     */
    public void callRequestingLogThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                keepRequestingLog = true;
                while (keepRequestingLog) {
                    try {
                        Platform.runLater(() -> {
                            try {
                                constantRefreshLog();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        });

                        Thread.sleep(2000);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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
     * @throws AccessDeniedException        - may be thrown.
     * @throws UserWarnedException          - may be thrown.
     */
    public void constantRefresh() throws ExecutionException, InterruptedException,
            NoStudentPermissionException, AccessDeniedException, UserWarnedException {
        questions = new PriorityQueue<>();
        questions.addAll(HomeSceneCommunication.constantlyGetQuestions(session.getRoomLink()));
        loadQuestions();

        if (session.isModerator()) {
            String linkId = session.getRoomLink();
            try {
                ServerCommunication.isRoomOpenStudents(linkId);
                changeImageOpenRoomButton();
            } catch (NoStudentPermissionException exception) {
                changeImageCloseRoomButton();
            }
        }

        //ServerCommunication.isTheRoomClosed(session.getRoomLink());
        if (!session.isModerator()) {
            //ServerCommunication.hasStudentPermission(session.getRoomLink());
            ServerCommunication.isRoomOpenStudents(session.getRoomLink());
            QuestionCommunication.updatesOnQuestions(session.getUserId(), session.getRoomLink());
            if (!session.isWarned()) {
                BanCommunication.isIpWarned(session.getRoomLink());
            } else {
                BanCommunication.isIpBanned(session.getRoomLink());
            }
        }
    }


    /**
     * This method is constantly called by a thread and refreshes the page.
     * @throws ExecutionException - may be thrown.
     * @throws InterruptedException - may be thrown.
     */
    public void constantRefreshLog() throws ExecutionException, InterruptedException {
        questions = new PriorityQueue<>();
        questions.addAll(HomeSceneCommunication
                .constantlyGetAnsweredQuestions(session.getRoomLink()));
        loadAnsweredQuestions();
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
                        .add(createQuestionCellLog(question, "/questionCellQuestionLog.fxml"));
            } catch (IOException e) {
                questionBoxLog.getChildren().add(
                        new Label("Something went wrong while loading this question"));
            }
        }
    }

    /**
     *  Creates a node for a question in the question log scene.
     * @param question - the question.
     * @param resource - the question cell.
     * @return - a Node of the question.
     */
    protected Node createQuestionCellLog(Question question, String resource) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
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

        return newQuestion;
    }

    /**
     * loads questions in the question box in the correct format.
     */
    public void loadQuestions() {

        String resource = "/questionCellStudent.fxml";
        if (session.isModerator()) {
            resource = "/questionCellModerator.fxml";
        }

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

    /**
     * Method to check whether the Question that is being created has been
     * written by the student in the session (so that the dismiss button
     * will be shown - if it does not correspond it won't show).
     *
     * @param newQuestion - Node of the new question created
     * @param question    - the object of the new question created
     */
    public void checkForQuestion(Node newQuestion, Question question) {
        if (!session.isModerator()) {
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


    public String buttonColour;

    /**
     * Transitions from Main question scene to Question log and vice versa.
     */
    public void controlQuestionLog() {
        if (mainBox.isVisible()) {
            questionButton.setStyle("-fx-background-color: white");
            keepRequesting = false;
            mainBox.setVisible(false);
            mainBoxLog.setVisible(true);
            callRequestingLogThread();
        } else {
            if (buttonColour == null) {
                buttonColour = "black";
            }
            questionButton.setStyle("-fx-background-color:" + buttonColour);
            keepRequestingLog = false;
            mainBoxLog.setVisible(false);
            mainBox.setVisible(true);
            callRequestingThread();
        }
    }

    /**
     * This method opens the settings window.
     * @throws IOException - may be thrown.
     */
    public void openSettings() throws IOException {
        SettingsController.initialize(this, darkTheme);
    }

    /**
     * This method changes the colour of the common objects in the main scenes.
     * @param mode - indicates if it's a darkTheme or not.
     * @param buttonColour - the colour of the buttons.
     * @param menuColour - the colour of the menu background.
     * @param textColour - the colour of the labels in the menu.
     * @param inputColour - the colour of the input box.
     * @param backgroundColour - the background colour.
     */
    public void changeTheme(
            boolean mode, String buttonColour, String menuColour,
             String textColour, String inputColour, String backgroundColour) {
        if (mode) {
            darkTheme = true;
        } else {
            darkTheme = false;
        }
        this.buttonColour = buttonColour;
        pane.setStyle("-fx-background-color:" + backgroundColour);
        sendButton.setStyle("-fx-background-color:" + inputColour);
        questionInput.setStyle("-fx-text-fill:" + inputColour);
        textBox.setStyle("-fx-background-color: transparent; -fx-border-color:" + inputColour);
        logLabel.setStyle("-fx-text-fill:" + textColour);
        settingsLabel.setStyle("-fx-text-fill:" + textColour);
        roomName.setStyle("-fx-text-fill:" + textColour);
        settingsButton.setStyle("-fx-background-color:" + buttonColour);
        if (mainBoxLog.isVisible()) {
            questionButton.setStyle("-fx-background-color: white");
        }
    }

}