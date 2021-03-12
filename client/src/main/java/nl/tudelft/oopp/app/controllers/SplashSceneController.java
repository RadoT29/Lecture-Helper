package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.communication.SplashCommunication;
import nl.tudelft.oopp.app.models.Room;

import java.io.IOException;

public class SplashSceneController {

    @FXML
    private TextField roomName;
    @FXML
    private TextField roomLink;
    @FXML
    private Button roleControl;
    @FXML
    private TextField nickName;
    @FXML
    private Button setNick;

    /**
     * Handles clicking the button.
     */
    public void buttonClicked() {

        Room room = SplashCommunication.postRoom(roomName.getText());

        String text = "Student link: " + room.linkIdStudent.toString()
                + "\nModerator link: " + room.linkIdModerator.toString();

        TextArea textArea = new TextArea(text);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        GridPane gridPane = new GridPane();
        gridPane.setMaxWidth(Double.MAX_VALUE);
        gridPane.add(textArea, 0, 0);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Stuff");
        alert.getDialogPane().setContent(gridPane);
        alert.showAndWait();

    }

    /**
     * Handles user roles.
     * @throws IOException - Is thrown if loader fails.
     */
    public void selectUserType() throws IOException {

        String userRole = SplashCommunication.checkForRoom(roomLink.getText());
        System.out.println("This worked - selectUserType!!!");
        Parent loader;
        if (userRole.equals("Student")) {
            System.out.println("You're a student");
            loader = new FXMLLoader(getClass().getResource("/studentScene.fxml")).load();
        } else if (userRole.equals("Moderator")) {
            System.out.println("You're a moderator");
            loader = new FXMLLoader(getClass().getResource("/nickName.fxml")).load();
        } else {
            return;
        }

        Stage stage = (Stage) roleControl.getScene().getWindow();
        Scene scene = new Scene(loader);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * For now Just goes to Moderator Scene.
     * @throws IOException - is thrown if loader fails.
     */
    public void setNickName() throws IOException {

        Parent loader = new FXMLLoader(getClass().getResource("/moderatorScene.fxml")).load();
        Stage stage = (Stage) setNick.getScene().getWindow();
        Scene scene = new Scene(loader);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        //TODO
        //Add name to user,but first we need to figure out how the client can store information.
    }

}
