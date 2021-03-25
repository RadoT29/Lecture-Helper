package nl.tudelft.oopp.app.controllers;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.tudelft.oopp.app.communication.BanCommunication;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.communication.SplashCommunication;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.NoSuchRoomException;
import nl.tudelft.oopp.app.exceptions.RoomIsClosedException;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Session;

import java.awt.*;
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
    @FXML
    private Button scheduleRoomButton;
    @FXML
    private Label invalidRoomLink;
    @FXML
    private Label invalidRoomName;
    @FXML
    private Label invalidNickName;
    @FXML
    private Label scheduleRoomFail;


    /**
     * Handles clicking the button.
     * @throws IOException Is thrown if loader fails.
     */
    public void createRoom() throws IOException {

        // Cannot create rooms with empty names
        if (roomName.getText().equals("")) {
            invalidRoomName.setVisible(true);
            return;
        }

        //Creates Room with given room name and clears input box
        Room room = SplashCommunication.postRoom(roomName.getText());
        roomName.clear();
        invalidRoomName.setVisible(false);

        //Creates a popup with the links
        LinkController linkController = new LinkController();
        linkController.getLinks(room.linkIdStudent.toString(), room.linkIdModerator.toString());


    }


    /**
     * Enters a room and goes to nickname scene.
     *
     * @throws IOException - Is thrown if loader fails.
     */
    public void enterRoom() throws IOException {
        Session.cleanUserSession();
        if (roomLink.getText().equals("")) {
            invalidRoomLink.setText("Insert a Room Link!");
            invalidRoomLink.setVisible(true);
            return;
        }

        try {
            //This method checks if the link inserted corresponds
            // to a Student one, Moderator one or if it is invalid.
            SplashCommunication.checkForRoom(roomLink.getText());

            //Gets the session with the updated information
            Session session = Session.getInstance();
            if (!session.getIsModerator()) {
                BanCommunication.saveStudentIp(session.getUserId(), roomLink.getText());
            }
            System.out.println("Is moderator3 " + session.getIsModerator());
            ServerCommunication.isTheRoomClosed(roomLink.getText());
            //System.out.println(roomLink.getText());

            ServerCommunication.isTheRoomClosed(session.getRoomLink());
            System.out.println("Is moderator " + session.getIsModerator());

            if (!session.getIsModerator()) {
                BanCommunication.isIpBanned(roomLink.getText());
                ServerCommunication.hasStudentPermission(roomLink.getText());
            }

            ServerCommunication.getRoomName();


            Parent loader = new FXMLLoader(getClass().getResource("/nickName.fxml")).load();
            Stage stage = (Stage) enterRoomButton.getScene().getWindow();
            Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            double width = screenSize.getWidth() * 0.8;
            double height = screenSize.getHeight() * 0.8;

            Scene scene = new Scene(loader, width, height);

            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        } catch (NoSuchRoomException exception) {
            invalidRoomLink.setText("No such room exists or the link is wrong!");
            invalidRoomLink.setVisible(true);

        } catch (RoomIsClosedException exception) {
            invalidRoomLink.setText("The room is closed!");
            invalidRoomLink.setVisible(true);

        } catch (NoStudentPermissionException exception) {
            invalidRoomLink.setText("No student permission to the Room!");
            invalidRoomLink.setVisible(true);
        } catch (AccessDeniedException exception) {
            invalidRoomLink.setText("Access denied!");
            invalidRoomLink.setVisible(true);
        }


    }

    /**
     * Handles user roles.
     *
     * @throws IOException - Is thrown if loader fails.
     */
    public void selectUserType() throws IOException {

        // Cannot enter without nickname
        if (nickName.getText().equals("")) {
            invalidNickName.setVisible(true);
            return;
        }

        Session session = Session.getInstance();

        // Sets nickname
        String userId = session.getUserId();
        ServerCommunication.setNick(userId, nickName.getText());

        Parent loader;
        // If the user is a moderator, loads the moderator moderatorScene,
        // otherwise loads the studentScene
        if (session.getIsModerator()) {

            loader = new FXMLLoader(getClass().getResource("/moderatorScene.fxml")).load();
        } else {
            loader = new FXMLLoader(getClass().getResource("/studentScene.fxml")).load();
        }

        Stage stage = (Stage) setNick.getScene().getWindow();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                Platform.exit();
                System.exit(0);
            }
        });

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.8;
        double height = screenSize.getHeight() * 0.8;

        Scene scene = new Scene(loader, width, height);

        // Sets the room name
        Label roomLabel = (Label) scene.lookup("#roomName");
        roomLabel.setText(session.getRoomName());

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * handles click on the scheduleRoomButton.
     * tries to load scheduleRoomScene
     * if fails the message under the button is shown
     */
    public void scheduleRoom() {
        try {
            ScheduleRoomSceneController.init();
        } catch (IOException e) {
            scheduleRoomFail.setVisible(true);
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
