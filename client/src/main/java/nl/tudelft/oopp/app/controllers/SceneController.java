package nl.tudelft.oopp.app.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.UserWarnedException;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.PriorityQueue;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public abstract class SceneController {
    protected boolean interruptThread = false;
    protected boolean openOne = true;
    protected String buttonColour;

    Session session = Session.getInstance();

    protected PriorityQueue<Question> questions = new PriorityQueue<>();

    protected boolean keepRequesting;

    protected boolean darkTheme;

    protected Thread thread;


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
     *
     * @throws ExecutionException   - may be thrown.
     * @throws InterruptedException - may be thrown.
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
}
