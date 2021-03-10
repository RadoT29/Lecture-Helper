package nl.tudelft.oopp.app.models;

import java.util.UUID;

public final class Session {

    private static Session instance;

    private String roomLink;
    private String roomName;
    private boolean isModerator;

    /**Session constructor.
     * @param roomLink Link for the room that is going to be used by this client for requests
     * @param roomName Name of the room
     * @param isModerator If this user is a moderator or students it will load different displays.
     *                    Notice there still is a server side authentication for the links,
     *                    so a student can not access moderator rights
     *                    just by changing this variable
     */
    public Session(String roomLink, String roomName, boolean isModerator) {
        this.roomLink = roomLink;
        this.roomName = roomName;
        this.isModerator = isModerator;
    }

    /**Get Instance/Instance constructor.
     * If there is not a singleton instance, creates a new one with the given variables.
     * In case there is already an instance returns the existing instance.
     * @param roomLink Link for the room that is going to be used by this client for requests
     * @param roomName Name of the room
     * @param isModerator If this user is a moderator or students it will load different displays.
     *                    Notice there still is a server side authentication for the links,
     *                    so a student can not access moderator rights
     *                    just by changing this variable
     * @return singleton class instance
     */
    public static Session getInstance(String roomLink, String roomName, boolean isModerator) {
        if (instance == null) {
            instance = new Session(roomLink, roomName, isModerator);
        }
        return instance;
    }

    /** Get session instance.
     * @return singleton class instance
     */
    public static Session getInstance() {
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

