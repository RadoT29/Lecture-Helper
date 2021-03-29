package nl.tudelft.oopp.app.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Feedback {
    public long id;
    public Room room;
    public String comment;

    public Feedback(String comment) {
        this.comment = comment;
    }


}
