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
import nl.tudelft.oopp.app.communication.ServerCommunication;
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
     * true/exception for close
     * false for open
     */
    public void closeOpenRoom() {
        //Session session = Session.getInstance();
        String linkId = session.getRoomLink();
        try {
            ServerCommunication.isRoomOpenStudents(linkId);
            changeImageCloseRoomButton();
            ServerCommunication.closeRoomStudents(linkId);

        } catch (NoStudentPermissionException exception) {
            changeImageOpenRoomButton();
            ServerCommunication.openRoomStudents(linkId);
        }
    }

    /**
     * change the image of the closeOpenRoomButton
     * to open room image.
     */
    public void changeImageCloseRoomButton() {
        closeOpenRoomButton.setStyle("-fx-shape: \"M184.646,0v21.72H99.704v433.358h31.403V53.123h"
                + "53.539V492.5l208.15-37.422v-61.235V37.5L184.646,0z M222.938,263.129\n"
                + "\t\tc-6.997,0-12.67-7.381-12.67-16.486c0-9.104,5.673-16.485,12.67-16.4"
                + "85s12.67,7.381,12.67,16.485\n"
                + "\t\tC235.608,255.748,229.935,263.129,222.938,263.129z\"");
        closeOpenRoomLabel.setText("Open Room");
    }

    /**
     * change the image of the closeOpenRoomButton
     * to close room image.
     */
    public void changeImageOpenRoomButton() {
        closeOpenRoomButton.setStyle("-fx-shape: \"M32.6652 5.44421C17.6121 5.44421 5.44434 17.611"
                + "9 5.44434 32.6651C5.44434 47.7182 17.6121 59.8859 32.6652 59.8859C47.7183 59.88"
                + "59 59.886 47.7182 59.886 32.6651C59.886 17.6119 47.7183 5.44421 32.6652 5.44421"
                + "ZM32.6652 54.4417C20.6608 54.4417 10.8885 44.6694 10.8885 32.6651C10.8885 20.66"
                + "07 20.6608 10.8884 32.6652 10.8884C44.6696 10.8884 54.4418 20.6607 54.4418 32.6"
                + "651C54.4418 44.6694 44.6696 54.4417 32.6652 54.4417ZM42.4375 19.0546L32.6652 28"
                + ".8269L22.8929 19.0546L19.0548 22.8928L28.827 32.6651L19.0548 42.4373L22.8929 46"
                + ".2755L32.6652 36.5032L42.4375 46.2755L46.2756 42.4373L36.5033 32.6651L46.2756 2"
                + "2.8928L42.4375 19.0546Z\"");
        closeOpenRoomLabel.setText("Close Room");
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
