package nl.tudelft.oopp.app.models;

import java.util.UUID;

public class AdminRoom {

    public String id;
    public String name;
    public UUID linkIdStudent;
    public UUID linkIdModerator;
    public boolean open;
    public boolean permission;
    public int numberQuestionsInterval;
    public int timeInterval;
    public String startDate;
    public String createdAt;
    public String updatedAt;
}
