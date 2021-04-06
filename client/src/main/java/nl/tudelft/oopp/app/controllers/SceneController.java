package nl.tudelft.oopp.app.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.UserWarnedException;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public abstract class SceneController implements Initializable {
    protected boolean interruptThread = false;
    protected boolean openOne = true;
    protected String buttonColour;

    Session session = Session.getInstance();

    protected PriorityQueue<Question> questions = new PriorityQueue<>();

    protected boolean keepRequesting;

    protected boolean darkTheme;

    protected Thread thread;

    @FXML
    protected VBox questionBoxLog;
    @FXML
    protected AnchorPane pane;
    @FXML
    public Button settingsButton;
    @FXML
    public Label settingsLabel;

    /**
     * This method initializes the thread,
     * which is responsible for constantly refreshing the questions.
     *
     * @param url - The path.
     * @param rb  - Provides any needed resources.
     */
    public void initialize(URL url, ResourceBundle rb) {
        thread = new Thread(new Runnable() {
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

                        Thread.sleep(10000);
                        if (interruptThread) {
                            Thread.currentThread().interrupt();
                            break;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    /**
     * This method is constantly called by a thread and refreshes the page.
     * @throws ExecutionException - Exception thrown when attempting to retrieve
     *                              the result of a task that aborted by throwing an exception.
     * @throws InterruptedException - Thrown when a thread is waiting, sleeping,
     *                                or otherwise occupied, and the thread is interrupted,
     *                                either before or during the activity.
     * @throws NoStudentPermissionException - Thrown if the user is a student
     *                                        has no permission for the room.
     * @throws AccessDeniedException - Thrown if the Server denies access to user.
     * @throws UserWarnedException - Thrown if the user has been warned by a moderator.
     */
    public abstract void constantRefresh()
            throws ExecutionException, InterruptedException,
            NoStudentPermissionException, AccessDeniedException, UserWarnedException;

    protected void changeScene(String resource, double dimensionScale) {
        Parent loader = null;
        try {
            loader = new FXMLLoader(getClass().getResource(resource)).load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = session.getStage();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * dimensionScale;
        double height = screenSize.getHeight() * dimensionScale;

        Scene scene = new Scene(loader, width, height);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
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

        changeScene("/splashScene.fxml", 0.8);

        openOne = false;
    }

    /**
     * This method opens the settings window.
     * @throws IOException - may be thrown.
     */
    public void openSettings() throws IOException {
        SettingsController.initialize(this, darkTheme);
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
                        .add(createQuestionCellLog(question));
            } catch (IOException e) {
                questionBoxLog.getChildren().add(
                        new Label("Something went wrong while loading this question"));
            }
        }
    }

    /**
     * Creates a node for a question in the question log scene.
     * @param question - the question.
     * @return - a Node of the question.
     */
    protected Node createQuestionCellLog(Question question) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/questionCellQuestionLog.fxml"));
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
     * This method alters the colour theme of the application.
     * @param mode - indicates if we are in dark mode or not.
     */
    public void changeTheme(boolean mode) {
        darkTheme = mode;
        String backgroundAdd;
        String backgroundRemove;
        String labelAdd;
        String labelRemove;
        String buttonAdd;
        String buttonRemove;

        if (mode) {
            backgroundAdd = "darkPane";
            backgroundRemove = "lightPane";
            buttonAdd = "menuBtnDark";
            buttonRemove = "menuBtnBlack";
            labelAdd = "labelDark";
            labelRemove = "labelBlack";
            buttonColour = "menuBtnDark";
        } else {
            backgroundAdd = "lightPane";
            backgroundRemove = "darkPane";
            buttonAdd = "menuBtnBlack";
            buttonRemove = "menuBtnDark";
            labelAdd = "labelBlack";
            labelRemove = "labelDark";
            buttonColour = "menuBtnBlack";
        }
        changeColours(backgroundAdd,backgroundRemove,
                        labelAdd, labelRemove, buttonAdd, buttonRemove);
    }

    /**
     * This method applies the colour changes.
     * @param backgroundAdd - the colour of the pane.
     * @param backgroundRemove - the previous colour of the pane.
     * @param labelAdd - the colour of the label.
     * @param labelRemove - the previous colour of the label.
     * @param buttonAdd - the colour of the button.
     * @param buttonRemove - the previous colour of the button.
     */
    private void changeColours(String backgroundAdd, String backgroundRemove,
                               String labelAdd, String labelRemove,
                               String buttonAdd, String buttonRemove) {
        pane.getStyleClass().removeAll(Collections.singleton(backgroundRemove));
        pane.getStyleClass().add(backgroundAdd);
        settingsButton.getStyleClass().removeAll(Collections.singleton(buttonRemove));
        settingsButton.getStyleClass().add(buttonAdd);
        settingsLabel.getStyleClass().removeAll(Collections.singleton(labelRemove));
        settingsLabel.getStyleClass().add(labelAdd);
    }

    public abstract void refresh();
}
