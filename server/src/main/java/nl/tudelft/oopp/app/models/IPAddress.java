package nl.tudelft.oopp.app.models;


import javax.persistence.*;

@Entity
public class IPAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String ipAddress;

    private boolean access;

    @ManyToOne
    private Room roomId;

    @OneToOne
    private User userId;

    public IPAddress(String ipAddress, Room roomId, User userId) {
        this.ipAddress = ipAddress;
        this.access = true;
        this.roomId = roomId;
        this.userId = userId;
    }

    public IPAddress() {

    }
}
