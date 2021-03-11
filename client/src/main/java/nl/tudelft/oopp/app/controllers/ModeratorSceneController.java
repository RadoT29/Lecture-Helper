package nl.tudelft.oopp.app.controllers;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import javafx.scene.control.Button;
import javafx.util.Duration;

import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class ModeratorSceneController implements Initializable {

    @FXML
    private AnchorPane navBar;
    @FXML
    private Button menuOpen;
    @FXML
    private Button menuClose;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navBar.setTranslateX(-200);

    }

    public void openMenu(MouseEvent event) {

        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(navBar);

        slide.setToX(0);
        slide.play();

        navBar.setTranslateX(-200);

        slide.setOnFinished((ActionEvent e) -> {
            menuOpen.setStyle("-fx-background-color: #ffffff;");
        });
    }

    public void closeMenu(MouseEvent event) {

        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.4));
        slide.setNode(navBar);

        slide.setToX(-200);
        slide.play();

        navBar.setTranslateX(0);

        slide.setOnFinished((ActionEvent e) -> {
            menuOpen.setStyle("-fx-background-color: #000000;");
        });
    }


}
