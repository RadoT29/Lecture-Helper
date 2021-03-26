package nl.tudelft.oopp.app.models;


import javax.persistence.*;

@Entity
public class IpAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String ipAddress;

    private boolean access;

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
        this.access = true;
        this.roomId = roomId;
        this.userId = userId;
    }

    public IpAddress() {

    }
}
