package nl.tudelft.oopp.app.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * This class controls the Main scene of the Moderators.
 */
public class ModeratorSceneController implements Initializable {
    @FXML
    public Button menuButton;
    @FXML
    public VBox mainMenu;
    @FXML
    public VBox slidingMenu;

    private TranslateTransition openNav;
    private TranslateTransition closeNav;
    private TranslateTransition closeFastNav;

    /**
     * This method initializes the state of the navigation bar.
     * It hides the sliding bar behind the regular one.
     * @param url - The path.
     * @param rb - Provides any needed resources.
     */
    @Override
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

}
