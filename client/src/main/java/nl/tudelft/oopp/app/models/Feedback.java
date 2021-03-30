package nl.tudelft.oopp.app.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Feedback {
    public long id;
    public Room room;
    public int rating;
    public String comment;

    public Feedback(String comment, int rating) {
        this.comment = comment;
        this.rating = rating;
    }


    public String toString() {
        return "Stars: " + rating + "/5\n\n" + comment;
    }
}
