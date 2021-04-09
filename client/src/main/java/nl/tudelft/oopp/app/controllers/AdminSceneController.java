package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.communication.AdminCommunication;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.models.AdminRoom;

import java.awt.*;
import java.io.FileWriter;
import java.util.*;

import java.io.IOException;

public class AdminSceneController  {

    private ArrayList<AdminRoom> rooms;
    private HashMap<String,AdminRoom> selected;

    @FXML
    private VBox roomBox;
    @FXML
    private Button selectAllButton;

    /**
     * Loads Admin Scene on the given stage.
     * @param stage stage where the Scene has to be loaded
     * @throws IOException - Thrown if an error occurs during loading
     */
    public static void init(Stage stage) throws IOException {

        Parent loader = new FXMLLoader(AdminPasswordController.class
                .getResource("/adminScene.fxml")).load();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.8;
        double height = screenSize.getHeight() * 0.8;

        Scene scene = new Scene(loader, width, height);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * This method makes a request for the rooms on database to refresh them on the client.
     */
    public void refresh() {

        rooms = new ArrayList<>();
        selected = new HashMap<>();
        rooms.addAll(AdminCommunication.getAdminRooms());
        loadRooms();
        selectAllButton.setText("Select All");
    }

    /**
     * This method loads the room cells in the given container.
     */
    public void loadRooms() {

        roomBox.getChildren().clear();

        for (AdminRoom room: rooms) {
            try {
                roomBox.getChildren()
                        .add(createRoomCell(room));
            } catch (IOException e) {
                roomBox.getChildren().add(
                        new Label("Something went wrong while loading this room"));
            }
        }
    }

    /**
     * This method creates RoomCells with the information from the given rooms.
     * @param room The room that needs to be used to converted
     * @return A Node that displays all the information of the room
     * @throws IOException - Thrown if an error occurs during loading
     */
    private Node createRoomCell(AdminRoom room) throws IOException {

        String resource = "/roomCell.fxml";

        // load the room to a newNode and set it's adminSceneController to this
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Node newRoom = loader.load();
        RoomCellController rcc = loader.getController();
        rcc.setAdminScene(this);

        //set the node id to the question id
        newRoom.setId(room.id);


        TextField idText = (TextField) newRoom.lookup("#idText");
        idText.setText(room.id);

        TextField nameText = (TextField) newRoom.lookup("#nameText");
        nameText.setText(room.name);

        TextField moderatorLinkText = (TextField) newRoom.lookup("#moderatorLinkText");
        moderatorLinkText.setText(room.linkIdModerator.toString());

        TextField studentLinkText = (TextField) newRoom.lookup("#studentLinkText");
        studentLinkText.setText(room.linkIdStudent.toString());

        TextField isOpenText = (TextField) newRoom.lookup("#isOpenText");
        isOpenText.setText(Boolean.toString(room.open));

        TextField permissionText = (TextField) newRoom.lookup("#permissionText");
        permissionText.setText(Boolean.toString(room.permission));

        TextField maxQuestTText = (TextField) newRoom.lookup("#maxQTText");
        maxQuestTText.setText(Integer.toString(room.numberQuestionsInterval));

        TextField timeIntervalText = (TextField) newRoom.lookup("#timeIntervalText");
        timeIntervalText.setText(Integer.toString(room.timeInterval));

        TextField startDateText = (TextField) newRoom.lookup("#startDateText");
        startDateText.setText(room.startDate);


        TextField createdAtText = (TextField) newRoom.lookup("#createdAtText");
        createdAtText.setText(room.createdAt);

        TextField updatedAtText = (TextField) newRoom.lookup("#updatedAtText");
        updatedAtText.setText(room.updatedAt);

        return newRoom;
    }

    public HashMap<String, AdminRoom> getSelected() {
        return selected;
    }

    public ArrayList<AdminRoom> getRooms() {
        return rooms;
    }

    /**
     * Deletes all the question in the selected rooms.
     */
    public void clearQuestion() {

        for (AdminRoom room: selected.values()) {
            String moderatorLink = room.linkIdModerator.toString();
            HomeSceneCommunication.clearQuestions(moderatorLink);
        }
        refresh();
    }

    /**
     * Set Max Question per Time constraints on all the given rooms.
     * @throws IOException - Thrown if an error occurs while loading the Question per time popup
     */
    public void setConstraints() throws IOException {
        QuestionsPerTimeAdminController constraintsController =
                new QuestionsPerTimeAdminController();
        constraintsController.open(selected, this);
    }

    /**
     * Exports the questions for all the selected rooms.
     */
    public void exportQuestions() {

        for (AdminRoom room: selected.values()) {

            String moderatorLink = room.linkIdModerator.toString();

            String exported = HomeSceneCommunication.exportQuestions(moderatorLink);

            try {

                FileWriter file = new FileWriter(("ExportedQuestions"
                        + room.name + ".txt"));
                assert exported != null;
                file.write(exported);
                file.close();

            } catch (IOException e) {

                System.out.print("Impossible to find file");
                e.printStackTrace();
            }
        }
        refresh();
    }

    /**
     * Unbans all users in all the selected rooms.
     */
    public void unbanUsers() {

        for (AdminRoom room: selected.values()) {
            String moderatorLink = room.linkIdModerator.toString();
            AdminCommunication.unbanAllUsersForRoom(moderatorLink);
        }
        refresh();
    }

    /**
     * Selects/Deselect all the rooms.
     */
    public void selectAll() {

        if (selectAllButton.getText().equals("Select All")) {
            selectAllButton.setText("Deselect All");
        } else {
            selectAllButton.setText("Select All");
        }

        for (Node roomCell: roomBox.getChildren()) {
            CheckBox selectedBox = (CheckBox) roomCell.lookup("#selectedBox");
            selectedBox.fire();
        }
    }

    /**
     * Closes all selected rooms.
     */
    public void closeRooms() {

        for (AdminRoom room: selected.values()) {
            String moderatorLink = room.linkIdModerator.toString();
            ServerCommunication.closeRoomStudents(moderatorLink);
        }
        refresh();
    }

    /**
     * Opens all selected rooms.
     */
    public void openRooms() {

        for (AdminRoom room: selected.values()) {
            String moderatorLink = room.linkIdModerator.toString();
            ServerCommunication.openRoomStudents(moderatorLink);
        }
        refresh();
    }

}
