package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import nl.tudelft.oopp.app.models.AdminRoom;

import java.util.ArrayList;
import java.util.HashMap;

public class RoomCellController {

    @FXML
    private CheckBox selectedBox;

    private AdminSceneController asc;

    public void setAdminScene(AdminSceneController asc) {
        this.asc = asc;
    }

    /**
     * Adds or removes the room to the selected ones after clicking on the checkbox.
     */
    public void onSelected() {

        HashMap<String, AdminRoom> listOfSelected = asc.getSelected();
        ArrayList<AdminRoom> rooms = asc.getRooms();

        String selectedId = selectedBox.getParent().getId();

        for (AdminRoom room: rooms) {
            if (room.id.equals(selectedId)) {
                listOfSelected.put(selectedId,room);
                return;
            }
        }
    }
}
