package nl.tudelft.oopp.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.models.Session;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;

import java.awt.*;
import java.io.IOException;

public class QuestionsPerTimeController {
    private Session session = Session.getInstance();

    @FXML
    public CheckBox noLimitsCheckBox;

    @FXML
    private CheckBox setLimitsCheckBox;

    @FXML
    private HBox constraintsHBox;

    @FXML
    private TextField questionsField;

    @FXML
    private TextField minutesField;

    //private Stage linkStage;

    /**
     * When the check box for no limits is clicked these commands are executed.
     */
    public void noLimitsCheckBoxMethod() {
        setLimitsCheckBox.setSelected(false);
        noLimitsCheckBox.setSelected(true);
        constraintsHBox.setVisible(false);
    }

    /**
     * When the check box for set limits is clicked these commands are executed.
     */
    public void setLimitsCheckBoxMethod() {
        noLimitsCheckBox.setSelected(false);
        setLimitsCheckBox.setSelected(true);
        constraintsHBox.setVisible(true);
    }

    /**
     * This method set the constraints as call the method
     * setQuestionsPerTime in HomeSceneCommunication
     * which make the request to the server.
     * @param actionEvent - the scene event
     */
    public void setConstraints(ActionEvent actionEvent) {

        if (noLimitsCheckBox.isSelected()) {
            sendAllConstraints(Integer.MAX_VALUE, Integer.MAX_VALUE);

        } else if (setLimitsCheckBox.isSelected()) {
            sendAllConstraints(Integer.parseInt(questionsField.getText()),
                            Integer.parseInt(minutesField.getText()));
        }

        Window window = ((Node) (actionEvent.getSource())).getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).close();
        }

    }

    /**
     * Open the scene for setting the number of questions per time.
     * @throws IOException - may thrown
     */
    public void open() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/QuestionsPerTime.fxml"));
        loader.setController(this);
        Parent parent = loader.load();

        Stage linkStage = new Stage();

        Scene scene = new Scene(parent);

        linkStage.setScene(scene);
        linkStage.show();
    }

    public void sendAllConstraints(int numQuestion, int minutes) {
        HomeSceneCommunication.setQuestionsPerTime(numQuestion,minutes,session.getRoomLink());
    }
}
