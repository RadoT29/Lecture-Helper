package nl.tudelft.oopp.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.models.AdminRoom;

import java.io.IOException;
import java.util.HashMap;

public class QuestionsPerTimeAdminController extends QuestionsPerTimeController {

    private HashMap<String, AdminRoom> selected;
    private AdminSceneController asc;

    /**
     * Opens the scene for setting the number of questions per time.
     * @param selected  Selected rooms where the change needs to be applied
     * @throws IOException - may thrown
     */
    public void open(HashMap<String, AdminRoom> selected,
                     AdminSceneController asc) throws IOException {

        this.selected = selected;
        this.asc = asc;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/QuestionsPerTime.fxml"));
        loader.setController(this);
        Parent parent = loader.load();

        Stage linkStage = new Stage();
        Scene scene = new Scene(parent);
        linkStage.setScene(scene);
        linkStage.show();
    }

    public void setConstraints(ActionEvent actionEvent) {
        super.setConstraints(actionEvent);
        asc.refresh();
    }

    /**
     * Updates the constraints for all the select rooms with the given constraints.
     * @param numQuestion how many questions
     * @param minutes over how many minutes
     */
    @Override
    public void sendAllConstraints(int numQuestion, int minutes) {

        for (AdminRoom room: selected.values()) {
            String moderatorLink = room.linkIdModerator.toString();
            HomeSceneCommunication.setQuestionsPerTime(numQuestion, minutes, moderatorLink);
        }
    }

}
