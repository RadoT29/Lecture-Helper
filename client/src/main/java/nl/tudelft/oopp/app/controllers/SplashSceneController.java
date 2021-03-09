package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.models.Room;

public class SplashSceneController {

    @FXML
    private TextField roomName;

    /**
     * Handles clicking the button.
     */
    public void buttonClicked() {

        Room room = ServerCommunication.postRoom(roomName.getText());

        String text = "Student link: " + room.linkIdStudent.toString()
                + "\nModerator link: " + room.linkIdModerator.toString();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setWidth(900);
        alert.setHeight(300);
        alert.setTitle("Room links");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
