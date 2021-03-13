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
import javafx.scene.control.Label;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.models.Session;

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
    @FXML
    private Label nameOfRoom;

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
     *
     * @throws IOException - Is thrown if loader fails.
     */
    public void selectUserType() throws IOException {

        //This method checks if the link inserted corresponds
        // to a Student one, Moderator one or if it is invalid.
        SplashCommunication.checkForRoom(roomLink.getText());

        Parent loader;

        //Gets the session with the updated information
        Session session = Session.getInstance();

        //If the link is not valid then no session is started and user should stay on splash screen
        if (session == null) {
            System.out.println("Insert valid link");
            return;
        }

        // If the user is a moderator, loads the moderator moderatorScene,
        // otherwise loads the studentScene
        if (session.getIsModerator()) {
            loader = new FXMLLoader(getClass().getResource("/moderatorScene.fxml")).load();
        } else {
            loader = new FXMLLoader(getClass().getResource("/studentScene.fxml")).load();
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
