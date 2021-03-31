package nl.tudelft.oopp.app.models;


import javax.persistence.*;

@Entity
public class IpAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String ipAddress;

    //status 1 - everything is fine with the user
    //status 0 - the user is warned
    //status -1 - user is banned
    private int status;

    @ManyToOne
    private Room roomId;

    @OneToOne
    private User userId;

    /**
     * Constructor for that class.
     * @param ipAddress - the ipAddress of the user
     * @param roomId - the room where the user is
     * @param userId - the user object/ the user which it is
     */

    public IpAddress(String ipAddress, Room roomId, User userId) {
        this.ipAddress = ipAddress;
        this.status = 1;
        this.roomId = roomId;
        this.userId = userId;
    }

    public IpAddress() {

    }
}
