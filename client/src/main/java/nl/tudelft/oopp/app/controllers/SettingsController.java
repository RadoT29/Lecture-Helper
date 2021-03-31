package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SettingsController {

    @FXML
    public Button themeButtonDark;

    @FXML
    public Button themeButtonLight;

    @FXML
    public Button exitButton;

    @FXML
    public Label themeLabel;

    @FXML
    public AnchorPane settingsPane;

    private HomeSceneController hsc;

    private final String menuColour = "#17AEDA";


    public void setHsc(HomeSceneController hsc) {
        this.hsc = hsc;
    }

    /**
     * This method initializes the settings window.
     * @param hsc - the controller, from which the settings button was clicked.
     * @param mode - the current theme.
     * @throws IOException - may be thrown.
     */
    public static void initialize(HomeSceneController hsc, boolean mode) throws IOException {
        FXMLLoader loader = new FXMLLoader(SettingsController
                .class.getResource("/settingsScene.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        SettingsController controller = loader.getController();

        controller.setHsc(hsc);

        if (mode) {
            controller.setDark();
        } else {
            controller.setLight();
        }


        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    /**
     * This method sets the theme to dark.
     */
    public void setDark() {
        exitButton.setStyle("-fx-background-color:" + menuColour);
        themeButtonDark.setVisible(false);
        themeButtonLight.setVisible(true);
        String darkModeMenu = "#282a36";
        settingsPane.setStyle("-fx-background-color:" +  darkModeMenu);
        themeLabel.setText("Light Mode");
        String darkModeText = "#ffb86c";
        themeLabel.setStyle("-fx-text-fill:" + darkModeText);

        String inputText = "white";
        String darkModeBackground = "#44475a";
        hsc.changeTheme(true, menuColour, darkModeMenu,
                darkModeText, inputText, darkModeBackground);
    }

    /**
     * This method sets the theme to light.
     */
    public void setLight() {
        exitButton.setStyle("-fx-background-color: black");
        themeButtonLight.setVisible(false);
        themeButtonDark.setVisible(true);
        settingsPane.setStyle("-fx-background-color: #17AEDA");
        themeLabel.setText("Dark Mode");
        themeLabel.setStyle("-fx-text-fill: black");

        String text = "black";
        String background = "#f4f4f4";
        hsc.changeTheme(false, text, menuColour, text, text, background);
    }

    public void exit() {
        Stage stage = (Stage) themeButtonDark.getScene().getWindow();
        stage.close();
    }
}
