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
import javafx.stage.Window;
import nl.tudelft.oopp.app.communication.*;
import nl.tudelft.oopp.app.exceptions.*;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.communication.QuestionCommunication;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.communication.SplashCommunication;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.OutOfLimitOfQuestionsException;
import nl.tudelft.oopp.app.exceptions.RoomIsClosedException;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.QuestionsUpdate;
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

    protected PriorityQueue<Question> questions;

    protected boolean keepRequesting;

    protected boolean keepRequestingLog;

    protected boolean darkTheme;

    /**
     * This method initializes the thread,
     * which is responsible for constantly refreshing the questions.
     *
     * @param url - The path.
     * @param rb  - Provides any needed resources.
     */
    public void initialize(URL url, ResourceBundle rb) {

        callRequestingThread();
    }

    /**
     * This thread will periodically refresh the content of the question queue.
     */
    public void callRequestingThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                keepRequesting = true;
                while (keepRequesting) {
                    try {
                        Platform.runLater(() -> {
                            try {
                                constantRefresh();
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            } catch (UserWarnedException e) {
                                //Pops up a message
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setWidth(900);
                                alert.setHeight(300);
                                alert.setTitle("Warning!");
                                alert.setHeaderText("Banning warning!");
                                alert.showAndWait();
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
     * @throws UserWarnedException          - may be thrown.
     */
    public void constantRefresh() throws ExecutionException, InterruptedException,
            NoStudentPermissionException, RoomIsClosedException,
            AccessDeniedException, UserWarnedException {
        questions = new PriorityQueue<>();
        
        //used to update the upvotecount locally considering moderator upVotes
        List<Question> unsortedQuestions =
                HomeSceneCommunication.constantlyGetQuestions(session.getRoomLink());
        for (Question q: unsortedQuestions) {
            q.setUpVotesFinal(getTotalUpVotes(q));
        }
        
        questions.addAll(unsortedQuestions);
        loadQuestions();
        ServerCommunication.isTheRoomClosed(session.getRoomLink());
        if (!session.getIsModerator()) {
            ServerCommunication.hasStudentPermission(session.getRoomLink());
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
        if (session.getIsModerator()) {
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
     * Every 2 seconds the client side of the app asks the server for a
     * questions update for this user. If there is one, this method
     * is called by the QuestionCommunication class.
     * @param result - depending on the update, the result can be -1 for
     *               a question discarded or 0 for question marked
     *               as answered. Depending on that a pop up appears and
     *               notifies the user for its question update.
     */
    public static void questionUpdatePopUp(QuestionsUpdate result) {

        //String[] updateInformation = result.split("/");
        String additionalText = "";

        String text = "";
        if (result.getStatusQuestion() == -1) {
            text = "Your question has been discarded!";
            additionalText = "Your question: \"" + result.getQuestionText()
                    + "\" has been discarded!";
        } else if (result.getStatusQuestion() == 0) {
            text = "Your question has been marked as answered!";
            additionalText = "Your question: \"" + result.getQuestionText()
                    + "\" has been marked as answered!";
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setWidth(900);
        alert.setHeight(300);
        alert.setTitle("Update on your question!");
        alert.setHeaderText(text);
        alert.setContentText(additionalText);
        alert.showAndWait();

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