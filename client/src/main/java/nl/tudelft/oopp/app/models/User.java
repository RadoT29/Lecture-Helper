package nl.tudelft.oopp.app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor

public class User {
    public long id;
    public String name;
    public Room roomId;
    public boolean isModerator;
    //  private Date createdAt;
    //  private Date updatedAt;

    /**
     * Constructor for User class.
     * @param id - Id for each user
     * @param name - userName used by a user
     * @param roomId - roomId of the room where user is
     * @param isModerator - boolean to indicate if user is a moderator
     */
    public User(long id,String name, Room roomId, boolean isModerator) {
        this.id = id;

        this.name = name;

        this.roomId = roomId;

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
    //    public boolean getIsModerator() {
    //        return isModerator;
    //    }



}
