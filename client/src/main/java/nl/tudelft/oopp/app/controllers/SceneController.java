package nl.tudelft.oopp.app.controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
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
    private boolean interruptThread = false;
    private boolean openOne = true;
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
     * This method is constantly called by a thread and refreshes the page.
     * @throws ExecutionException           - may be thrown.
     * @throws InterruptedException         - may be thrown.
     */
    public abstract void constantRefresh()
            throws ExecutionException, InterruptedException;

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

}
