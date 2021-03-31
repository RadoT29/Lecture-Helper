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
import nl.tudelft.oopp.app.communication.PollCommunication;
import nl.tudelft.oopp.app.communication.ReactionCommunication;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.communication.SplashCommunication;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.RoomIsClosedException;
import nl.tudelft.oopp.app.models.*;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

/**
 * This class controls the Main scene of the Students.
 */
public class StudentPollSceneController implements Initializable {

    @FXML
    public Button reactionButton;
    @FXML
    public Button speedButton;
    @FXML
    public VBox speedMenu;
    @FXML
    public VBox reactionMenu;

    @FXML
    public Button fastButton;
    @FXML
    public Button okButton;
    @FXML
    public Button slowButton;

    @FXML
    public Button happyButton;
    @FXML
    public Button sadButton;
    @FXML
    public Button confusedButton;

    @FXML
    public VBox pollBox;

    protected List<Poll> polls;
    Session session = Session.getInstance();

    private TranslateTransition openSpeedNav;
    private TranslateTransition closeSpeedNav;
    private TranslateTransition closeSpeedFastNav;

    private TranslateTransition openReactionNav;
    private TranslateTransition closeReactionNav;
    private TranslateTransition closeReactionFastNav;

    /**
     * This method initializes the state of the navigation bar.
     * It hides the sliding bars behind the regular one.
     * @param url - The path.
     * @param rb - Provides any needed resources.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        openSpeedNav = new TranslateTransition(Duration.millis(100), speedMenu);
        openSpeedNav.setToX(speedMenu.getTranslateX() - speedMenu.getWidth());
        closeSpeedNav = new TranslateTransition(Duration.millis(100), speedMenu);
        closeSpeedFastNav = new TranslateTransition(Duration.millis(.1), speedMenu);

        openReactionNav = new TranslateTransition(Duration.millis(100), reactionMenu);
        openReactionNav.setToX(reactionMenu.getTranslateX() - reactionMenu.getWidth());
        closeReactionNav = new TranslateTransition(Duration.millis(100), reactionMenu);
        closeReactionFastNav = new TranslateTransition(Duration.millis(.1), reactionMenu);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                closeSpeedFastNav.setToX(-(speedMenu.getWidth()));
                closeSpeedFastNav.play();
                closeReactionFastNav.setToX(-(reactionMenu.getWidth()));
                closeReactionFastNav.play();
            }
        });

    }

    /**
     * This method closes the sliding part of the speed bar.
     */
    public void hideSpeedBar() {
        speedButton.getStyleClass().remove("speedBtnWhite");
        speedButton.getStyleClass().add("speedBtnBlack");
        closeSpeedNav.setToX(-(speedMenu.getWidth()));
        closeSpeedNav.play();
    }

    /**
     * This method checks the current state of the speed bar.
     * Afterwards, it decides whether to close or open the speed bar.
     */
    public void controlSpeedMenu() {
        if ((reactionMenu.getTranslateX()) != -(reactionMenu.getWidth())) {
            hideReactionBar();
        }
        if ((speedMenu.getTranslateX()) == -(speedMenu.getWidth())) {
            speedButton.getStyleClass().remove("speedBtnBlack");
            speedButton.getStyleClass().add("speedBtnWhite");
            openSpeedNav.play();
        } else {
            hideSpeedBar();
        }
    }

    /**
     * This method closes the sliding part of the reaction bar.
     */
    public void hideReactionBar() {

        reactionButton.getStyleClass().remove("reactionBtnWhite");
        reactionButton.getStyleClass().add("reactionBtnBlack");
        closeReactionNav.setToX(-(reactionMenu.getWidth()));
        closeReactionNav.play();
    }

    /**
     * This method checks the current state of the reaction bar.
     * Afterwards, it decides whether to close or open the reaction bar.
     */
    public void controlReactionMenu() {
        if ((speedMenu.getTranslateX()) != -(speedMenu.getWidth())) {
            hideSpeedBar();
        }
        if ((reactionMenu.getTranslateX()) == -(reactionMenu.getWidth())) {
            reactionButton.getStyleClass().remove("reactionBtnBlack");
            reactionButton.getStyleClass().add("reactionBtnWhite");
            openReactionNav.play();
        } else {
            hideReactionBar();
        }
    }

    /**
     * This method sends the corresponding SpeedReaction when the fastButton is clicked.
     */
    public void tooFast() {
        ReactionCommunication.postReaction(new SpeedReaction(1));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        fastButton.setDisable(true);
        okButton.setDisable(false);
        slowButton.setDisable(false);
    }


    /**
     * This method sends the corresponding SpeedReaction when the okButton is clicked.
     */
    public void fastEnough() {
        ReactionCommunication.postReaction(new SpeedReaction(0));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        fastButton.setDisable(false);
        okButton.setDisable(true);
        slowButton.setDisable(false);
    }

    /**
     * This method sends the corresponding SpeedReaction when the slowButton is clicked.
     */
    public void tooSlow() {
        ReactionCommunication.postReaction(new SpeedReaction(-1));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        fastButton.setDisable(false);
        okButton.setDisable(false);
        slowButton.setDisable(true);
    }

    /**
     * This method sends the corresponding EmotionReaction when the happyButton is clicked.
     */
    public void addHappy() {
        ReactionCommunication.postReaction(new EmotionReaction(1));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        happyButton.setDisable(true);
        sadButton.setDisable(false);
        confusedButton.setDisable(false);
    }

    /**
     * This method sends the corresponding EmotionReaction when the sadButton is clicked.
     */
    public void addSad() {
        ReactionCommunication.postReaction(new EmotionReaction(0));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        happyButton.setDisable(false);
        sadButton.setDisable(true);
        confusedButton.setDisable(false);
    }

    /**
     * This method sends the corresponding EmotionReaction when the confusedButton is clicked.
     */
    public void addConfused() {
        ReactionCommunication.postReaction(new EmotionReaction(-1));

        // When a reaction is sent, it is disabled so that the same one can't be sent again
        happyButton.setDisable(false);
        sadButton.setDisable(false);
        confusedButton.setDisable(true);
    }

    public void goToPolls() throws IOException {
        Parent loader = new FXMLLoader(getClass().getResource("/studentPollScene.fxml")).load();
        Stage stage = (Stage) reactionMenu.getScene().getWindow();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.8;
        double height = screenSize.getHeight() * 0.8;

        Scene scene = new Scene(loader, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    //Poll stuff
    protected Node createPollOptionCell(PollOption pollOption, boolean isFinished) throws IOException {
        // load the poll to a newNode and set it's homeSceneController to this
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/studentPollOptionCell.fxml"));
        Node newPollOption = loader.load();

        //set the node id to the poll id
        newPollOption.setId("" + pollOption.getId());

        //set the poll text
        Label optionLabel = (Label) newPollOption.lookup("#optionText");
        optionLabel.setText(pollOption.getOptionText());

        if(isFinished) {
            CheckBox optionIsCorrect = (CheckBox) newPollOption.lookup("#isCorrect");
            optionIsCorrect.setDisable(true);
            if (pollOption.isCorrect()) {
                newPollOption.getStyleClass().add("highlighted");
            }
        }
        return newPollOption;
    }


    /**
     * creates a node for a question.
     * @param resource String the path to the resource with the question format
     * @return Node that is ready to be displayed
     * @throws IOException if the loader fails
     *      or one of the fields that should be changed where not found
     */
    protected Node createPollCell(Poll poll, String resource) throws IOException {
        // load the poll to a newNode and set it's homeSceneController to this
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Node newPoll = loader.load();
        StudentPollCellController qsc = loader.getController();
        qsc.setHomeScene(this);

        //set the node id to the poll id
        newPoll.setId("" + poll.getId());

        //set the poll text
        Label questionlabel = (Label) newPoll.lookup("#questionText");
        questionlabel.setText(poll.getQuestion());


        //set the question box size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.4;
        questionlabel.setPrefWidth(width);
        questionlabel.setMaxWidth(width);

        VBox pollOptionBox = (VBox) newPoll.lookup("#pollOptionBox");

        for (PollOption pollOption :
                poll.getPollOptions()) {
            pollOptionBox.getChildren().add(createPollOptionCell(pollOption, poll.isFinished()));
        }

        return newPoll;
    }


    /**
     * loads questions in the question box in the correct format.
     */
    public void loadPolls() {

        String resource = "/studentPollCell.fxml";
        if (session.getIsModerator()) {
            resource = "/studentPollCell.fxml";
        }

        polls = PollCommunication.getPolls();

        pollBox.getChildren().clear();
        for (Poll poll :
                polls) {
            try {
                if (poll.isOpen()) {
                    pollBox.getChildren()
                            .add(createPollCell(poll, resource));
                }
            } catch (IOException e) {
                System.out.println(e);
                pollBox.getChildren().add(
                        new Label("Something went wrong while loading this poll"));
            }
        }
    }


    /**
     * fill in the priority queue and and load them on the screen.
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
}

