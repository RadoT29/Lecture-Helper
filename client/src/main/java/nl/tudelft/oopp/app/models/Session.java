package nl.tudelft.oopp.app.models;

public final class Session {

    private static Session instance;

    private String roomLink;
    private String roomName;
    private boolean isModerator;
    private String userId;

    /**Session constructor.
     * @param roomLink Link for the room that is going to be used by this client for requests
     * @param roomName Name of the room
     * @param isModerator If this user is a moderator or students it will load different displays.
     *                    Notice there still is a server side authentication for the links,
     *                    so a student can not access moderator rights
     *                    just by changing this variable
     */
    public Session(String roomLink, String roomName, String userId, boolean isModerator) {
        this.roomLink = roomLink;
        this.roomName = roomName;
        this.isModerator = isModerator;
        this.userId = userId;
    }

    /**Session constructor.
     * @param roomLink Link for the room that is going to be used by this client for requests
     * @param userId Id of the user on the local client
     * @param isModerator If this user is a moderator or students it will load different displays.
     *                    Notice there still is a server side authentication for the links,
     *                    so a student can not access moderator rights
     *                    just by changing this variable
     */
    public Session(String roomLink, String userId, boolean isModerator) {
        this.roomLink = roomLink;
        this.userId = userId;
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
    public static Session getInstance(String roomLink,
                                      String roomName,
                                      String userId,
                                      boolean isModerator) {
        if (instance == null) {
            instance = new Session(roomLink, roomName, userId, isModerator);
        }
        return instance;
    }

    /**Get Instance/Instance constructor.
     * If there is not a singleton instance, creates a new one with the given variables.
     * In case there is already an instance returns the existing instance.
     * @param roomLink Link for the room that is going to be used by this client for requests
     * @param userId Id of the user on the local client
     * @param isModerator If this user is a moderator or students it will load different displays.
     *                    Notice there still is a server side authentication for the links,
     *                    so a student can not access moderator rights
     *                    just by changing this variable
     * @return singleton class instance
     */
    public static Session getInstance(String roomLink, String userId, boolean isModerator) {
        if (instance == null) {
            instance = new Session(roomLink, userId, isModerator);
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * This method resets the session by clearing all the session Data.
     */
    public void cleanUserSession() {
        isModerator = false;
        roomLink = null;
        roomName = null;
        userId = null;
    }
}

