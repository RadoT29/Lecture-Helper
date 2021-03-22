package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.models.Session;

import java.awt.*;

public class QuestionsPerTimeController {

    private Session session = Session.getInstance();

    @FXML
    public Checkbox noLimitsCheckBox;

    @FXML
    private Checkbox setLimitsCheckBox;

    @FXML
    private HBox constraintsHBox;

    @FXML
    private Button setQuestionsPerTimeButton;

    @FXML
    private TextField questionsField;

    @FXML
    private TextField minutesField;

    public void noLimitsCheckBoxMethod() {
        setLimitsCheckBox.setState(false);
        noLimitsCheckBox.setState(true);
        constraintsHBox.setVisible(false);
    }

    public void setLimitsCheckBoxMethod() {
        noLimitsCheckBox.setState(false);
        setLimitsCheckBox.setState(true);
        constraintsHBox.setVisible(true);
    }

    public void setConstraints() {
        if (noLimitsCheckBox.getState()) {
            //int a =Integer.MAX_VALUE;
            HomeSceneCommunication.setQuestionsPerTime(Integer.MAX_VALUE,Integer.MAX_VALUE, session.getRoomLink());
        } else if (setLimitsCheckBox.getState()) {
            HomeSceneCommunication.setQuestionsPerTime(Integer.parseInt(questionsField.getText()),Integer.parseInt(questionsField.getText()), session.getRoomLink());
        }
    }
}
