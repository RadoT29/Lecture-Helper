package nl.tudelft.oopp.app.models;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class TimeCount {

    public LocalTime roomTime;
    public LocalTime timeStamp;

    /**
     * Construtor for the Time count - this starts counting since the room was created.
     */
    public TimeCount() {
        this.roomTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    }






}
