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
import lombok.Setter;
import java.io.IOException;

@Setter
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

    private SceneController sc;

    /**
     * This method initializes the settings window.
     * @param sc - the controller, from which the settings button was clicked.
     * @param mode - the current theme.
     * @throws IOException - may be thrown.
     */
    public static void initialize(SceneController sc, boolean mode) throws IOException {
        FXMLLoader loader = new FXMLLoader(SettingsController
                .class.getResource("/settingsScene.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        SettingsController controller = loader.getController();

        controller.setSc(sc);

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
        String menuColour = "#17AEDA";
        exitButton.setStyle("-fx-background-color:" + menuColour);
        themeButtonDark.setVisible(false);
        themeButtonLight.setVisible(true);
        String darkModeMenu = "#282a36";
        settingsPane.setStyle("-fx-background-color:" +  darkModeMenu);
        themeLabel.setText("Light Mode");
        String darkModeText = "#ffb86c";
        themeLabel.setStyle("-fx-text-fill:" + darkModeText);

        sc.changeTheme(true);
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

        sc.changeTheme(false);
    }

    public void exit() {
        Stage stage = (Stage) themeButtonDark.getScene().getWindow();
        stage.close();
    }
}
