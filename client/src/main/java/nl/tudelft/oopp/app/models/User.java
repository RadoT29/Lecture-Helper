package nl.tudelft.oopp.app.models;

import java.util.Date;

public class User {
    public String id;
    public String name;
    public String roomLink;
    public boolean isModerator;
    //  private Date createdAt;
    //  private Date updatedAt;

    /**
     * Constructor for User class.
     * @param id - Id for each user
     * @param name - userName used by a user
     * @param roomLink - roomId of the room where user is
     * @param isModerator - boolean to indicate if user is a moderator
     */
    public User(String id,String name, String roomLink, boolean isModerator) {
        this.id = id;

        this.name = name;

        this.roomLink = roomLink;

        this.isModerator = isModerator;

    }




}
