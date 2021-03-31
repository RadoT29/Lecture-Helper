package nl.tudelft.oopp.app.controllers;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.communication.PollCommunication;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.communication.SplashCommunication;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.RoomIsClosedException;
import nl.tudelft.oopp.app.models.Poll;
import nl.tudelft.oopp.app.models.PollOption;
import nl.tudelft.oopp.app.models.Session;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ModeratorPollSceneController implements Initializable {

    Session session = Session.getInstance();

    @FXML
    private Label roomName;

    @FXML
    private VBox pollBox;

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

    protected List<Poll> polls;


    /**
     * This method initializes the thread,
     * which is responsible for constantly refreshing the questions.
     *
     * @param url - The path.
     * @param rb  - Provides any needed resources.
     */
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

        refresh();
    }

    /**
     * When this method is called it:
     * 1. set the boolean variable interruptThread = true
     * which afterwards interrupts the thread
     * 2. Open the Splash Scene and should close the current one
     */

    public void closeWindow() {
        Parent loader = null;
        try {
            loader = new FXMLLoader(getClass().getResource("/splashScene.fxml")).load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage linkStage = (Stage) roomName.getScene().getWindow();
        //Scene scene = new Scene(loader);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.8;
        double height = screenSize.getHeight() * 0.8;

        Scene scene = new Scene(loader, width, height);

        linkStage.setScene(scene);
        linkStage.centerOnScreen();
        linkStage.show();

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
     * Will be removed with refactoring.
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

    //Poll stuff

    /**
     * Create the poll options to fill the poll cell.
     * @param pollOption option data
     * @param optionCount place in the option order
     * @return a node to be loaded with the proper format and data
     * @throws IOException when the fxml file is not found
     */
    protected Node createPollOptionCell(PollOption pollOption, int optionCount) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/moderatorPollOptionCell.fxml"));
        Node newPollOption = loader.load();

        newPollOption.setId("" + optionCount);

        TextArea questionTextArea = (TextArea) newPollOption.lookup("#optionText");
        Text questionLabel = (Text) newPollOption.lookup("#optionLabel");
        CheckBox optionIsCorrect = (CheckBox) newPollOption.lookup("#isCorrect");
        questionTextArea.setText(pollOption.getOptionText());
        questionLabel.setText("Option " + optionCount);
        optionIsCorrect.setSelected(pollOption.isCorrect());

        return newPollOption;
    }

    /**
     * Create a new poll cell for a poll with its data.
     * @param poll the poll to load the cell
     * @return a node to be loaded with the proper format and data
     * @throws IOException when the fxml file is not found
     */
    protected Node createPollCell(Poll poll) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/moderatorPollCell.fxml"));
        Node newPoll = loader.load();
        ModeratorPollCellController qsc = loader.getController();
        qsc.setHomeScene(this);

        newPoll.setId("" + poll.getId());

        TextArea questionTextArea = (TextArea) newPoll.lookup("#questionText");
        questionTextArea.setText(poll.getQuestion());

        VBox pollOptionBox = (VBox) newPoll.lookup("#pollOptionBox");
        VBox resultBox = (VBox) newPoll.lookup("#resultBox");

        int optionCount = 1;
        for (PollOption pollOption :
                poll.getPollOptions()) {
            pollOptionBox.getChildren().add(createPollOptionCell(pollOption, optionCount));

            if (poll.isFinished()) {
                resultBox.getChildren().add(new Label("Option " + optionCount
                        + ":     " + pollOption.getScoreRate()));
            }

            optionCount++;
        }

        return newPoll;
    }

    /**
     * Load polls to the scene, creating new nodes for the poll box.
     */
    public void loadPolls() {
        polls = PollCommunication.getPolls();

        pollBox.getChildren().clear();
        for (Poll poll :
                polls) {
            try {
                pollBox.getChildren()
                        .add(createPollCell(poll));
            } catch (IOException e) {
                pollBox.getChildren().add(
                        new Label("Something went wrong while loading this poll"));
            }
        }
    }

    /**
     * Update the polls list and load them to the scene.
     */
    public void refresh() {
        polls = new ArrayList<>();
        polls.addAll(PollCommunication.getPolls());
        loadPolls();
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
        polls = new ArrayList<>();
        polls.addAll(PollCommunication.constantlyGetPolls(session.getRoomLink()));
        loadPolls();
        if (!session.getIsModerator()) {
            ServerCommunication.hasStudentPermission(session.getRoomLink());
        }

        ServerCommunication.isTheRoomClosed(session.getRoomLink());
        if (!session.getIsModerator()) {
            SplashCommunication.isIpBanned(session.getRoomLink());
        }


    }

    /**
     * Create a new empty poll on the server.
     */
    public void createPoll() {
        long pollId = PollCommunication.createPoll();
        refresh();
    }
}