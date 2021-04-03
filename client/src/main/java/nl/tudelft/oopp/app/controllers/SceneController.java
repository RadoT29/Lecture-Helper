package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

import java.awt.*;
import java.io.IOException;
import java.util.PriorityQueue;

public abstract class SceneController {
    private boolean interruptThread = false;
    private boolean openOne = true;
    protected String buttonColour;

    Session session = Session.getInstance();

    protected PriorityQueue<Question> questions = new PriorityQueue<>();

    protected boolean keepRequesting;

    protected boolean darkTheme;

    protected Thread thread;


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
