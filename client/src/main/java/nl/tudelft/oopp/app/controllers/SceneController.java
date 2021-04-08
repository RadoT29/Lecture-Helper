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
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
import java.util.List;
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
    @FXML
    public HBox textBox;
    @FXML
    public Label roomName;
    @FXML
    public Button sendButton;
    @FXML
    public TextField questionInput;
    @FXML
    public Label logLabel;

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
                                styleAlert(alert);
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
            FXMLLoader page = new FXMLLoader(getClass().getResource(resource));
            loader = page.load();
            SceneController controller = page.getController();
            controller.changeTheme(darkTheme);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = session.getStage();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * dimensionScale;
        double height = screenSize.getHeight() * dimensionScale;

        assert loader != null;
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
    protected void changeColours(String backgroundAdd, String backgroundRemove,
                               String labelAdd, String labelRemove,
                               String buttonAdd, String buttonRemove) {
        pane.getStyleClass().removeAll(Collections.singleton(backgroundRemove));
        pane.getStyleClass().add(backgroundAdd);
        settingsButton.getStyleClass().removeAll(Collections.singleton(buttonRemove));
        settingsButton.getStyleClass().add(buttonAdd);
        settingsLabel.getStyleClass().removeAll(Collections.singleton(labelRemove));
        settingsLabel.getStyleClass().add(labelAdd);
    }

    /**
     * This method applies the colour changes.
     * @param menuList - the menu components.
     * @param removeMenu - the previous menu colour.
     * @param addMenu - the new menu colour.
     * @param removeButton - the previous button colour.
     * @param addButton - the current button colour.
     * @param removeLabel - the previous label colour.
     * @param addLabel - the current label colour.
     */
    public void colourChange(List<VBox> menuList, String removeMenu,
                             String addMenu, String removeButton,
                             String addButton, String removeLabel, String addLabel) {
        for (VBox box : menuList) {
            box.getStyleClass().removeAll(Collections.singleton(removeMenu));
            box.getStyleClass().add(addMenu);
            for (Node node : box.getChildren()) {
                if (node instanceof Button) {
                    node.getStyleClass().removeAll(Collections.singleton(removeButton));
                    if (!node.getStyleClass().contains("menuBtnWhite")) {
                        node.getStyleClass().add(addButton);
                    }
                } else {
                    node.getStyleClass().removeAll(Collections.singleton(removeLabel));
                    node.getStyleClass().add(addLabel);
                }
            }
        }
    }

    /**
     * This method changes the theme of the main scene.
     * @param mode - indicates if we are in dark theme or not.
     */
    public void changeColourMainScene(boolean mode) {
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
    }

    /**
     * This method applies the colour changes of the main scene.
     * @param boxColourAdd - the colour of the input border.
     * @param boxColourRemove - the previous colour of the input border.
     * @param inputColourAdd - the colour of the input text.
     * @param inputColourRemove - the previous colour of the input box.
     * @param addLabel -the colour of the roomName label.
     * @param removeLabel - the previous colour of the roomName label.
     * @param buttonAdd -  the colour of the send button.
     * @param buttonRemove - the previous colour of the send button.
     */
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

    /**
     * This method changes the colour theme of the question log.
     * @param mode - indicates whether or not the mode is dark.
     */
    public void changeColourQuestionLog(boolean mode) {
        String addLabel;
        String removeLabel;

        if (mode) {
            addLabel = "labelDark";
            removeLabel = "labelBlack";
        } else {
            addLabel = "labelBlack";
            removeLabel = "labelDark";
        }

        logLabel.getStyleClass().removeAll(Collections.singleton(removeLabel));
        logLabel.getStyleClass().add(addLabel);
    }

    public abstract void refresh();

    protected static void styleAlert(Alert alert) {
        DialogPane dialogPane = alert.getDialogPane();
        javafx.scene.control.Button button = new Button();
        button.setStyle("-fx-min-width: 30; -fx-min-hight:30; -fx-background-color: #000; "
                + "-fx-shape: \"M11 7h2v2h-2zm0 4h2v6h-2zm1-9C6.48 "
                + "2 2 6.48 2 12s4.48 10 10 10 10-4.48 "
                + "10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 "
                + "8-8 8 3.59 8 8-3.59 8-8 8z\"");
        dialogPane.setStyle("-fx-background-color: #17AEDA");
        alert.setGraphic(button);
    }
}
