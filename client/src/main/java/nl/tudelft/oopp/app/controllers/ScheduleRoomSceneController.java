package nl.tudelft.oopp.app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.communication.SplashCommunication;
import nl.tudelft.oopp.app.models.Room;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ScheduleRoomSceneController {

    @FXML
    TextField roomName;
    @FXML
    TextField time;
    @FXML
    ComboBox<String> timeZoneComboBox;
    @FXML
    DatePicker date;
    @FXML
    CheckBox useLocalTimeZone;

    @FXML
    Label invalidRoomName;
    @FXML
    Label invalidTime;
    @FXML
    Label invalidTimeZone;
    @FXML
    Label invalidDate;
    @FXML
    Label dateInThePast;
    @FXML
    Label generateLinksError;


    /**
     * creates a new schedule room window.
     * @throws IOException if the loader failed
     */
    public static void init() throws IOException {
        FXMLLoader loader = new FXMLLoader(ScheduleRoomSceneController
                .class.getResource("/scheduleRoomScene.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        ScheduleRoomSceneController src = loader.<ScheduleRoomSceneController>getController();

        Label localTimeZone = (Label) scene.lookup("#localTimeZone");
        localTimeZone.setText(ZoneId.systemDefault().toString());

        Stage stage = new Stage();
        stage.setTitle("Schedule a room");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * checks if the user input in all the fields in the scene is valid
     * and if everything is valid the link scene is loaded.
     * otherwise a warning message appears where the mistake was found
     */
    public void generateLinks() {

        clearWarningLabels();

        // Cannot create rooms with empty names
        if (roomName.getText().equals("")) {
            invalidRoomName.setVisible(true);
            return;
        }

        //get entered date and time in local timeZone
        String timeString = time.getText();
        LocalTime localTime = getTimeFromString(timeString);
        LocalDate localDate = date.getValue();

        if (localTime == null) {
            invalidTime.setVisible(true);
            return;
        }
        if (localDate == null) {
            invalidDate.setVisible(true);
            return;
        }

        LocalDateTime localStartDate = LocalDateTime.of(localDate, localTime);

        // get time zone for the retrieved time and date
        ZoneId selectedTimeZone = getSelectedTimeZone();
        if (selectedTimeZone == null) {
            invalidTimeZone.setVisible(true);
            return;
        }

        //localStartDate with the selected time zone
        ZonedDateTime zonedStartDate = localStartDate.atZone(selectedTimeZone);

        //startDate in UTC and check if the date is not in the past
        ZonedDateTime utcStartDate = zonedStartDate.withZoneSameInstant(ZoneId.of("UTC"));
        if (utcStartDate.isBefore(ZonedDateTime.now(ZoneId.of("UTC")))) {
            dateInThePast.setVisible(true);
            return;
        }

        //Creates a popup with the links
        LinkController linkController = new LinkController();
        Room room = SplashCommunication.postRoom(roomName.getText());
        try {
            linkController.getLinks(room.linkIdStudent.toString(),room.linkIdModerator.toString());
        } catch (IOException e) {
            generateLinksError.setVisible(true);
        }

        // Close if success
        Stage stage = (Stage) roomName.getScene().getWindow();
        stage.close();

    }

    /**
     * Handles click on 'Cancel' button.
     * closes the schedule window.
     */
    public void cancelClicked() {
        Stage stage = (Stage) roomName.getScene().getWindow();
        stage.close();
    }

    /**
     * sets all warning labels to invisible.
     */
    public void clearWarningLabels() {
        invalidDate.setVisible(false);
        invalidRoomName.setVisible(false);
        invalidTime.setVisible(false);
        invalidTimeZone.setVisible(false);
        dateInThePast.setVisible(false);
        generateLinksError.setVisible((false));
    }

    /**
     * reads the selected time zone basing on user input in the scene.
     * if useLocalTimeZone is selected uses system's default time zone
     * else if no time zone is selected in the comboBox displays a warning message
     * if a time zone is selected returns that one
     * @return ZoneId picked by the user
     */
    public ZoneId getSelectedTimeZone() {
        //local time zone
        ZoneId selectedTimeZone = ZoneId.systemDefault();

        //use chosen time zone
        if (!useLocalTimeZone.isSelected()) {
            String timeZoneText = (String) timeZoneComboBox.getValue();
            if (timeZoneText == null) {
                return null;
            }
            // change to the opposite (idk why, but for some reasons it works the other way around)
            if (timeZoneText.contains("+")) {
                timeZoneText = timeZoneText.replace("+", "-");
            } else {
                timeZoneText = timeZoneText.replace("-", "+");
            }
            selectedTimeZone = ZoneId.of(timeZoneText);
        }
        System.out.println("Selected time zone: " + selectedTimeZone);
        return selectedTimeZone;
    }

    /**
     * transforms a String representation of time in format "HH:mm"
     * (first H zero does not have to be included)
     * into LocalTime.
     * @param timeString String representation of time "HH:mm"
     * @return LocalTime retrieved from the timeString
     */
    public LocalTime getTimeFromString(String timeString) {
        if (timeString.length() < 4) {
            return null;
        }
        if (timeString.charAt(1) == ':') {
            timeString = "0" + timeString;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        try {
            return LocalTime.parse(timeString, dtf);
        } catch (Exception e) {
            invalidTime.setVisible(true);
            return null;
        }
    }

    /**
     * handles change to useLocalTimeZone.
     * if this checkbox is selected then the comboBox for time zones is disabled.
     * when enabling comboBox again, adds time zones to its list
     */
    public void localTimeZoneChecked() {
        if (useLocalTimeZone.isSelected()) {
            timeZoneComboBox.setDisable(true);
            return;
        }
        addTimeZonesToComboBox();
        timeZoneComboBox.setDisable(false);
    }

    /**
     * adds the time zones to the comboBox list.
     */
    public void addTimeZonesToComboBox() {
        Set<String> allZones = ZoneId.getAvailableZoneIds();
        List<String> zoneList = new ArrayList<>(allZones);
        Collections.sort(zoneList);
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        for (String s : zoneList) {
            if ((s.contains("GMT-") || s.contains("GMT+"))) {
                timeZoneComboBox.getItems().add(s);
            }
        }
    }
}
