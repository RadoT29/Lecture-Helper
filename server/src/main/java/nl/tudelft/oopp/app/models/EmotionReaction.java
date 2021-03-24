package nl.tudelft.oopp.app.models;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@NoArgsConstructor
@Entity
public class EmotionReaction extends Reaction {


    // This constructor is for testing purposes
    public EmotionReaction(int value, Room room, User user) {
        super(value, room, user);
    }

}
