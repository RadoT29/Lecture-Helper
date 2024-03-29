package nl.tudelft.oopp.app.views;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import nl.tudelft.oopp.app.models.Session;

public class SplashDisplay extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Session.getInstance(primaryStage);

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.8;
        double height = screenSize.getHeight() * 0.8;

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/splashScene.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.getScene().getStylesheets().add("styles/splashScene.css");
        primaryStage.getScene().getStylesheets().add("styles/svgIcons.css");
        primaryStage.getIcons().add(new Image("assets/img/logo.png"));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
