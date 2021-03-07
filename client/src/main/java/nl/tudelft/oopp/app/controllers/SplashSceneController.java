package nl.tudelft.oopp.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.models.Room;

import java.io.IOException;

public class SplashSceneController {

    @FXML
    private TextField roomName;
    @FXML
    private TextField userType;
    @FXML
    private Button roleControl;
    @FXML
    private Label nameOfRoom;

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

    /**
     * Handles different users.
     */
    public void selectUserType() {
        FXMLLoader loader;
        if (userType.getText().equals("Student")) {
            loader = new FXMLLoader(getClass().getResource("/studentScene.fxml"));
        } else if (userType.getText().equals("Moderator")) {
            loader = new FXMLLoader(getClass().getResource("/moderatorScene.fxml"));
        } else {
            return;
        }

        try {
            Stage stage = (Stage) roleControl.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    /**
     * close the room
     */
    public void closeRoom() {
        ServerCommunication.closeRoom(nameOfRoom.getText());
    }

    /**
     * kick all students
     */
    public void kickAllStudents() {
        ServerCommunication.kickAllStudents(nameOfRoom.getText());
    }
}
