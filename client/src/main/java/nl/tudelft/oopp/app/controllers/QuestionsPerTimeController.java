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

    public void noLimitsCheckBoxMethod() {
        setLimitsCheckBox.setSelected(false);
        noLimitsCheckBox.setSelected(true);
        constraintsHBox.setVisible(false);
    }

    public void setLimitsCheckBoxMethod() {
        noLimitsCheckBox.setSelected(false);
        setLimitsCheckBox.setSelected(true);
        constraintsHBox.setVisible(true);
    }

    public void setConstraints(ActionEvent actionEvent) {
        if (noLimitsCheckBox.isSelected()) {
            //int a =Integer.MAX_VALUE;
            HomeSceneCommunication.setQuestionsPerTime(Integer.MAX_VALUE,Integer.MAX_VALUE, session.getRoomLink());
        } else if (setLimitsCheckBox.isSelected()) {
            HomeSceneCommunication.setQuestionsPerTime(Integer.parseInt(questionsField.getText()),Integer.parseInt(minutesField.getText()), session.getRoomLink());
        }
        Window window =   ((Node)(actionEvent.getSource())).getScene().getWindow();
        if (window instanceof Stage){
            ((Stage) window).close();
        }

    }

    public void open() throws IOException {
        Parent loader = new FXMLLoader(getClass().getResource("/QuestionsPerTime.fxml")).load();

        Stage linkStage = new Stage();

        Scene scene = new Scene(loader);

        linkStage.setScene(scene);
        linkStage.show();
    }
}
