package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.communication.SplashCommunication;
import nl.tudelft.oopp.app.exceptions.InvalidInputException;
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

        Label localTimeZone = (Label) scene.lookup("#localTimeZone");
        localTimeZone.setText(ZoneId.systemDefault().toString());

        Stage stage = new Stage();
        stage.setTitle("Schedule a room");
        stage.setScene(scene);
        stage.show();
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
        for (String s : zoneList) {
            if ((s.contains("GMT-") || s.contains("GMT+"))) {
                timeZoneComboBox.getItems().add(s);
            }
        }
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
    public void hideWarningLabels() {
        invalidDate.setVisible(false);
        invalidRoomName.setVisible(false);
        invalidTime.setVisible(false);
        invalidTimeZone.setVisible(false);
        dateInThePast.setVisible(false);
        generateLinksError.setVisible((false));
    }

    /**
     * checks if the user input in all the fields in the scene is valid
     * and if everything is valid the link scene is loaded.
     * otherwise a warning message appears where the mistake was found
     */
    public void generateLinks() {
        hideWarningLabels();
        // Cannot create rooms with empty names
        try {
            if (roomName.getText().equals("")) {
                throw new InvalidInputException(invalidRoomName);
            }
            LocalDateTime startDateUtc = getStartDateInUtc();
            //Creates a popup with the links
            LinkController linkController = new LinkController();
            Room room = SplashCommunication.scheduleRoom(roomName.getText(), startDateUtc);
            if (room == null) {
                throw new NullPointerException();
            }
            linkController.getLinks(room.linkIdStudent.toString(),room.linkIdModerator.toString());
        } catch (InvalidInputException | NullPointerException e) {
            return;
        } catch (IOException e) {
            generateLinksError.setVisible(true);
            return;
        }
        // Close if success
        Stage stage = (Stage) roomName.getScene().getWindow();
        stage.close();
    }

    /**
     * reads user's input in the fields related to the date and time.
     * using that input generates a startDateUtc
     * @return LocalDateTime picked by the user and transformed to the UTC time zone
     * @throws InvalidInputException if user's input is invalid
     */
    private LocalDateTime getStartDateInUtc() throws InvalidInputException {
        //get entered date and time in local timeZone
        LocalTime localTime = getTimeFromString(time.getText());
        LocalDate localDate = date.getValue();

        if (localDate == null) {
            throw new InvalidInputException(invalidDate);
        }
        //entered date time (without time zone)
        LocalDateTime localStartDate = LocalDateTime.of(localDate, localTime);

        // get time zone for the retrieved time and date
        ZoneId selectedTimeZone = getSelectedTimeZone();

        //localStartDate with the selected time zone
        ZonedDateTime zonedStartDate = localStartDate.atZone(selectedTimeZone);

        //startDate in UTC and check if the date is not in the past
        ZonedDateTime startDateUtc = zonedStartDate.withZoneSameInstant(ZoneId.of("UTC"));
        if (startDateUtc.isBefore(ZonedDateTime.now(ZoneId.of("UTC")))) {
            throw new InvalidInputException(dateInThePast);
        }
        return startDateUtc.toLocalDateTime();
    }



    /**
     * reads the selected time zone basing on user input in the scene.
     * if useLocalTimeZone is selected uses system's default time zone
     * else if no time zone is selected in the comboBox displays a warning message
     * if a time zone is selected returns that one
     * @return ZoneId picked by the user
     * @throws InvalidInputException user has not picked a timeZone correctly
     */
    public ZoneId getSelectedTimeZone() throws InvalidInputException {
        //local time zone
        ZoneId selectedTimeZone = ZoneId.systemDefault();
        if (useLocalTimeZone.isSelected()) {
            return selectedTimeZone;
        }
        // use a chosen timezone
        try {
            String timeZoneText = timeZoneComboBox.getValue();
            // change to the opposite (idk why, but for some reasons it works the other way around)
            if (timeZoneText.contains("+")) {
                timeZoneText = timeZoneText.replace("+", "-");
            } else {
                timeZoneText = timeZoneText.replace("-", "+");
            }
            selectedTimeZone = ZoneId.of(timeZoneText);
            return selectedTimeZone;
        } catch (Exception e) {
            throw new InvalidInputException(invalidTimeZone);
        }
    }

    /**
     * transforms a String representation of time in format "HH:mm"
     * (first H zero does not have to be included)
     * into LocalTime.
     * @param timeString String representation of time "HH:mm"
     * @return LocalTime retrieved from the timeString
     * @throws InvalidInputException if timeString is in incorrect format
     */
    public LocalTime getTimeFromString(String timeString) throws InvalidInputException {
        try {
            if (timeString.charAt(1) == ':') {
                timeString = "0" + timeString;
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
            return LocalTime.parse(timeString, dtf);
        } catch (Exception e) {
            throw new InvalidInputException(invalidTime);
        }
    }


}
