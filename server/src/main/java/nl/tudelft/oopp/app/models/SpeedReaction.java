package nl.tudelft.oopp.app.models;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@NoArgsConstructor
@Entity
public class SpeedReaction extends Reaction {

    // This constructor is for testing purposes
    public SpeedReaction(int value, Room room, User user) {
        super(value, room, user);
    }

    public SpeedReaction(int value) {
        super(value);
    }
}
