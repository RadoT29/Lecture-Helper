package nl.tudelft.oopp.app.views;

import java.awt.*;
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SplashDisplay extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();

        FXMLLoader loader = new FXMLLoader();
        URL xmlUrl = getClass().getResource("/splashScene.fxml");
        loader.setLocation(xmlUrl);
        Parent root = loader.load();

        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.getScene().getStylesheets().add("styles/splashScene.css");
        primaryStage.getScene().getStylesheets().add("styles/svgIcons.css");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
