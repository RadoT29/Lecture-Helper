package nl.tudelft.oopp.app.models;

public class Moderator extends User {
    /**
     * Constructor for Moderator.
     * @param id - same as user
     * @param name - same as user
     * @param roomLink - same as user
     * @param isModerator - same as user
     */
    public Moderator(String id,String name, String roomLink, boolean isModerator) {
        super(id, name, roomLink, isModerator);
    }

}
