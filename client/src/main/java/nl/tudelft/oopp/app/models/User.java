package nl.tudelft.oopp.app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor

public class User {
    public String id;
    public String name;
    public String roomLink;
    public boolean isModerator;
    //  private Date createdAt;
    //  private Date updatedAt;

    /**
     * Constructor for User class.
     *
     * @param id          - Id for each user
     * @param name        - userName used by a user
     * @param roomLink    - roomId of the room where user is
     * @param isModerator - boolean to indicate if user is a moderator
     */
    public User(String id, String name, String roomLink, boolean isModerator) {
        this.id = id;

        this.name = name;

        this.roomLink = roomLink;

        this.isModerator = isModerator;

    }

    //    public Long getUserID() {
    //        return id;
    //    }
    //
    //    public Room getRoomId() {
    //        return roomId;
    //    }
    //
    //    public String getUserName() {
    //        return name;
    //    }
    //
    public boolean getIsModerator() {
        return isModerator;
    }


}
