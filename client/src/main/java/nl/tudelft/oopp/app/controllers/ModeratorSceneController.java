package nl.tudelft.oopp.app.controllers;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.communication.ReactionCommunication;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.UserWarnedException;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

/**
 * This class controls the Main scene of the Moderators.
 */
public class ModeratorSceneController extends SceneController {
    @FXML
    public Button menuButton;
    @FXML
    public VBox mainMenu;
    @FXML
    public VBox slidingMenu;
    @FXML
    public Label moreOptionsLabel;

    @FXML
    public Button moreReactionButton;


    private ModeratorReactionController reactionController;
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

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                closeFastNav.setToX(-(slidingMenu.getWidth()));
                closeFastNav.play();
            }


        });
        super.initialize(url,rb);
        reactionController = new ModeratorReactionController(emotionReactions);
        refresh();
    }


    /**
     * This method closes the sliding part of the navigation bar.
     */
    public void hideSlidingBar() {
        if (buttonColour == null) {
            buttonColour = "black";
        }
        menuButton.setStyle("-fx-background-color:" + buttonColour);
        closeNav.setToX(-(slidingMenu.getWidth()));
        closeNav.play();
    }

    /**
     * This method checks the current state of the navigation bar.
     * Afterwards, it decides whether to close or open the navigation bar.
     */
    public void controlMenu() {

        if ((slidingMenu.getTranslateX()) == -(slidingMenu.getWidth())) {
            menuButton.setStyle("-fx-background-color: white");
            openNav.play();
        } else {
            hideSlidingBar();
        }
    }

    /**
     * close the room.
     */
    public void closeOpenRoom() {
        super.closeOpenRoom();
    }

    /**
     * Pressing the sendButton will send all the text in the questionInput
     * to the sever as a Question object.
     */
    public void sendQuestion() {
        super.sendQuestion();
    }




    /**
     * Method to clear all questions and allow the moderator to reset the room
     * It will activate when the clear questions button is clicked (within the moderator view)
     * It will reconfirm if the user is a moderator and call clearQuestions
     * in the HomeCommunicationScene.
     */
    public void clearQuestionsClicked() { 

        if (session.isModerator()) {
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
        
        String exported = "Nothing has been added";
        if (session.isModerator()) {
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
        changeScene("/presentationScene.fxml", 0.65);
    }

    /**
     * Method to load the poll scene.
     * @throws IOException if it cant load the fxml file
     */
    public void goToPolls() throws IOException {
        changeScene("/moderatorPollScene.fxml", 0.8);
    }

    public void goToLog() throws IOException {
        changeScene("/moderatorLogScene.fxml", 0.8);
        System.out.println("went to log");
    }

    /**
     * Method to load the main moderator scene.
     * Makes the scene bigger so the moderator can interacat with all its features
     * @throws IOException if it cant load the fxml file
     */
    public void goToHome() throws IOException {
        changeScene("/moderatorQuestionScene.fxml", 0.8);
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

    /**
     * shows feedback from students in the scene.
     */
    public void showFeedback() {
        try {
            ViewFeedbackSceneController.init();
        } catch (IOException e) {
            return;
        }


    }

    /**
     * handles click on moreReactionButton.
     * shows/hides the emotion reactions on the screen
     * and changes the button to + or -
     */
    public void moreReactionsClicked() {
        if (!emotionReactions.isVisible()) {
            //display emotion reactions
            moreReactionButton.getStyleClass().set(1, "hideButton");
            reactionController.showEmotion();
        } else {
            //hide emotion reactions
            moreReactionButton.getStyleClass().set(1, "expandButton");
            reactionController.hideEmotion();
        }

    }

    public abstract void constantRefresh()
            throws ExecutionException, InterruptedException,
            NoStudentPermissionException, AccessDeniedException, UserWarnedException{

    }


}
