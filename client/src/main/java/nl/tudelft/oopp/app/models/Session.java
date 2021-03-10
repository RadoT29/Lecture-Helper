package nl.tudelft.oopp.app.models;

import java.util.UUID;

public final class Session {

    private static Session instance;

    private String roomLink;
    private String roomName;
    private boolean isModerator;

    public Session(String roomLink, String roomName, boolean isModerator) {
        this.roomLink = roomLink;
        this.roomName = roomName;
        this.isModerator = isModerator;
    }

    public static Session getInstace(String roomLink, String roomName, boolean isModerator) {
        if (instance == null) {
            instance = new Session(roomLink, roomName, isModerator);
        }
        return instance;
    }
    public static Session getInstace() {
        return instance;
    }

    public String getRoomLink() {
        return roomLink;
    }

    public String getRoomName() {
        return roomName;
    }

    public boolean getIsModerator() {
        return isModerator;
    }

    public void cleanUserSession() {
        roomLink = null;
        roomName = null;
    }
}

