package nl.tudelft.oopp.app.models;

import java.util.UUID;

public class Room {
    public String name;
    public UUID linkIdStudent;
    public UUID linkIdModerator;
    public boolean isOpen;
    public boolean permission;

    public Room(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public UUID getLinkIdStudent() {
        return linkIdStudent;
    }

    public UUID getLinkIdModerator() {
        return linkIdModerator;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isPermission() {
        return permission;
    }
}
