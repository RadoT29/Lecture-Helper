package nl.tudelft.oopp.app.models;

import java.util.UUID;

public class AdminRoom {

    public String id;
    public String name;
    public UUID linkIdStudent;
    public UUID linkIdModerator;
    public boolean isOpen;
    public boolean permission;
    public int numberQuestionsInterval;
    public int timeInterval;
    public String startDate;
    public String endDateForStudents;
    public String createdAt;
    public String updatedAt;
}
