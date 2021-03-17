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
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.NoSuchRoomException;
import nl.tudelft.oopp.app.exceptions.RoomIsClosedException;
import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.communication.ServerCommunication;

import java.io.IOException;

public class SplashSceneController {

    @FXML
    private TextField roomName;
    @FXML
    private TextField roomLink;
    @FXML
    private TextField nickName;
    @FXML
    private Button setNick;
    @FXML
    private Button enterRoomButton;

    /**
     * Handles clicking the button.
     */
    public void createRoom() {

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
        alert.setTitle("Room Links");
        alert.getDialogPane().setContent(gridPane);
        alert.showAndWait();


    }

    /**
     * Enters a room and goes to nickname scene.
     *
     * @throws IOException - Is thrown if loader fails.
     */
    public void enterRoom() throws IOException {

        try {
            //This method checks if the link inserted corresponds
            // to a Student one, Moderator one or if it is invalid.
            SplashCommunication.checkForRoom(roomLink.getText());
            //Gets the session with the updated information
            Session session = Session.getInstance();
            ServerCommunication.isTheRoomClosed(session.getRoomLink());
            System.out.println("Is moderator " + session.getIsModerator());
            if (!session.getIsModerator()) {
                ServerCommunication.hasStudentPermission(session.getRoomLink());
            }


            Parent loader = new FXMLLoader(getClass().getResource("/nickName.fxml")).load();
            Stage stage = (Stage) enterRoomButton.getScene().getWindow();
            Scene scene = new Scene(loader);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
        } catch (NoSuchRoomException exception) {
            String text = "No such room exists or the link is wrong!";
            TextArea textArea = new TextArea(text);
            textArea.setWrapText(true);
            GridPane gridPane = new GridPane();
            gridPane.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(textArea, 0, 0);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("The room does not exist!");
            alert.getDialogPane().setContent(gridPane);
            alert.showAndWait();
        } catch (RoomIsClosedException exception) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("The room is closed!");
            //alert.getDialogPane()
            // .setContent("With this link, you already do not have permission to the room");
            alert.showAndWait();
        } catch (NoStudentPermissionException exception) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("No student permission to the Room!");
            //alert.getDialogPane()
            // .setContent("With this link, you already do not have permission to the room");
            alert.showAndWait();
        }


    }

    /**
     * Handles user roles.
     *
     * @throws IOException - Is thrown if loader fails.
     */
    public void selectUserType() throws IOException {

        Parent loader;
        Session session = Session.getInstance();
        // If the user is a moderator, loads the moderator moderatorScene,
        // otherwise loads the studentScene
        if (session.getIsModerator()) {
            loader = new FXMLLoader(getClass().getResource("/moderatorScene.fxml")).load();
        } else {
            loader = new FXMLLoader(getClass().getResource("/studentScene.fxml")).load();
        }

        Stage stage = (Stage) setNick.getScene().getWindow();
        Scene scene = new Scene(loader);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

        if (nickName.getText() != null) {
            String userId = session.getUserId();
            ServerCommunication.setNick(userId, nickName.getText());
            //setUserClass(nickName.getText());
        }


    }

    /* **
     * Establishes the subclass of the user in the session
     * @param name - This parameter corresponds to the user nickname set on the previous method
     *//*
    public void setUserClass(String name){
    Session session = Session.getInstance();

    if(session.getIsModerator()) {
    User moderator = new Moderator(session.getUserId(), name, session.get);
    } else {
    User student = new Student();
    }

    }
    */


}
