package nl.tudelft.oopp.app.controllers;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.communication.ReactionCommunication;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

/**
 * This class controls the Main scene of the Moderators.
 */
public class ModeratorSceneController extends HomeSceneController implements Initializable {
    @FXML
    public Button menuButton;
    @FXML
    public VBox mainMenu;
    @FXML
    public VBox slidingMenu;

    @FXML
    public Button speedStat;
    @FXML
    public Button emotionStat;


    private TranslateTransition openNav;
    private TranslateTransition closeNav;
    private TranslateTransition closeFastNav;

    /**
     * This method initializes the state of the navigation bar.
     * It hides the sliding bar behind the regular one.
     * @param url - The path.
     * @param rb - Provides any needed resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        openNav = new TranslateTransition(Duration.millis(100), slidingMenu);
        openNav.setToX(slidingMenu.getTranslateX() - slidingMenu.getWidth());
        closeNav = new TranslateTransition(Duration.millis(100), slidingMenu);
        closeFastNav = new TranslateTransition(Duration.millis(.1), slidingMenu);

        mainBoxLog.setVisible(false);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                closeFastNav.setToX(-(slidingMenu.getWidth()));
                closeFastNav.play();
            }


        });

        callSuperInitializeAndUpdateStats(url, rb);
    }

    /**
     * Calls the initialize of HomeSceneController.
     * @param url - The path.
     * @param rb - Provides any needed resources.
     */
    public void callSuperInitializeAndUpdateStats(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        updateStats();
    }

    /**
     * This method calls a thread to keep refreshing the reaction status.
     */
    public void updateStats() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                keepRequesting = true;
                while (keepRequesting) {
                    try {
                        Platform.runLater(() -> {
                            loadStats();
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
     * This method closes the sliding part of the navigation bar.
     */
    public void hideSlidingBar() {

        menuButton.getStyleClass().remove("menuBtnWhite");
        menuButton.getStyleClass().add("menuBtnBlack");
        closeNav.setToX(-(slidingMenu.getWidth()));
        closeNav.play();
    }

    /**
     * This method checks the current state of the navigation bar.
     * Afterwards, it decides whether to close or open the navigation bar.
     */
    public void controlMenu() {

        if ((slidingMenu.getTranslateX()) == -(slidingMenu.getWidth())) {
            menuButton.getStyleClass().remove("menuBtnBlack");
            menuButton.getStyleClass().add("menuBtnWhite");
            openNav.play();
        } else {
            hideSlidingBar();
        }
    }

    /**
     * close the room.
     */
    public void closeRoom() {
        super.closeRoom();
    }

    /**
     * kick all students.
     */
    public void kickAllStudents() {
        super.kickAllStudents();
    }

    /**
     * Pressing the sendButton will send all the text in the questionInput
     * to the sever as a Question object.
     */
    public void sendQuestion() {
        super.sendQuestion();
    }

    /**
     * fill in the priority queue and and load them on the screen.
     */
    public void refresh() {
        super.refresh();
        loadStats();
    }

    /**
     * This method changes the icons that represent the Speed and Emotion Reaction to
     * match their statistics.
     */
    public void loadStats() {
        int emotionStatInt = ReactionCommunication.getReactionStats(false);
        int speedStatInt = ReactionCommunication.getReactionStats(true);

        System.out.println("emotion = " + emotionStatInt);
        System.out.println("speed = " + speedStatInt);
        if (emotionStatInt == 1) {
            emotionStat.getStyleClass().set(1, "happyButton");
            System.out.println(emotionStat.getStyleClass());
        } else if (emotionStatInt == -1) {
            emotionStat.getStyleClass().set(1,"confusedButton");
        } else {
            emotionStat.getStyleClass().set(1,"sadButton");
        }

        if (speedStatInt == 1) {
            speedStat.getStyleClass().set(1,"fastButton");
        } else if (speedStatInt == -1) {
            speedStat.getStyleClass().set(1,"slowButton");
        } else {
            speedStat.getStyleClass().set(1,"okButton");
        }

    }

    /**
     * Method to clear all questions and allow the moderator to reset the room
     * It will activate when the clear questions button is clicked (within the moderator view)
     * It will reconfirm if the user is a moderator and call clearQuestions
     * in the HomeCommunicationScene.
     */
    public void clearQuestionsClicked() {
        Session session = Session.getInstance();

        if (session.getIsModerator()) {
            HomeSceneCommunication.clearQuestions(session.getRoomLink());
        }

        refresh();
    }

    /**
     * When the export questions is clicked it will send a request to get a
     * String with all the questions and their respective answers
     * it will then create a new text file inside the repository with these.
     */
    public void exportQuestionsClicked() {
        Session session = Session.getInstance();
        String exported = "Nothing has been added";
        if (session.getIsModerator()) {
            exported = HomeSceneCommunication.exportQuestions(session.getRoomLink());
        }

        try {
            FileWriter file = new FileWriter(new File("ExportedQuestions"
                                                    + session.getRoomName() + ".txt"));
            file.write(exported);
            file.close();

        } catch (IOException e) {
            System.out.print("Impossible to find file");
            e.printStackTrace();
        }
        refresh();
    }




    /**
     * Method to load the presentation mode scene.
     * Makes the scene smaller so it takes less space on the lecturer screen
     * @throws IOException if it cant load the fxml file
     */
    public void presenterMode() throws IOException {
        Parent loader = new FXMLLoader(getClass().getResource("/presentationScene.fxml")).load();
        Stage stage = (Stage) mainMenu.getScene().getWindow();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.65;
        double height = screenSize.getHeight() * 0.6;

        Scene scene = new Scene(loader, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * This method opens the scene where are inserted the
     * number of questions per time.
     * @throws IOException - may thrown
     */
    public void openConstraintsScene() throws IOException {
        QuestionsPerTimeController questionsPerTimeController = new QuestionsPerTimeController();
        questionsPerTimeController.open();

    }

    @Override
    protected Node createQuestionCellLog(Question question, String resource) throws IOException {
        Node newQuestion = super.createQuestionCellLog(question,resource);
        Label answerLabel = (Label) newQuestion.lookup("#answerTextLabel");

        if (!answerLabel.getText().equals("This question was answered during the lecture")) {
            Button answerButtonLog = (Button) newQuestion.lookup("#answerButtonLog");
            answerButtonLog.getStyleClass().remove("answerButton");
            answerButtonLog.getStyleClass().add("editButton");
            answerButtonLog.setText("Edit");
        }
        return newQuestion;
    }
}
