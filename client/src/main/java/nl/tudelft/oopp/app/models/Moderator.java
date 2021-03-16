package nl.tudelft.oopp.app.models;

public class Moderator extends User {
    /**
     * Constructor for Moderator.
     * @param id - same as user
     * @param name - same as user
     * @param roomId - same as user
     * @param isModerator - same as user
     */
    public Moderator(long id,String name, Room roomId, boolean isModerator) {
        super(id, name, roomId, isModerator);
    }

}
