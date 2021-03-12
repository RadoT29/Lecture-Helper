package nl.tudelft.oopp.app.models;

import javax.persistence.Entity;

/**
 * This class represents a moderator of the application.
 * The moderator is a type of user and is stored in the database.
 */
@Entity
public class Moderator extends User {

    /**
     * Create a moderator.
     *
     * @param name   - name of moderator
     * @param roomId - the room, which the moderator enters.
     */
    public Moderator(String name, Room roomId) {
        super(name, roomId);
    }

    /**
     * Empty constructor.
     * (Intellij insists that this has to be created)
     */
    public Moderator(Room roomId) {
        super(roomId, true);
    }

    public Moderator() {
        super();
    }

    @Override
    public boolean equals(Object other) {
        if (super.equals(other)
                && other instanceof Moderator) {
            return true;
        }

        return false;
    }
}
