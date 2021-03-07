package nl.tudelft.oopp.app.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "link_id_student")
    private UUID linkIdStudent;

    @Column(name = "link_id_moderator")
    private UUID linkIdModerator;

    @Column(name = "is_open")
    private boolean isOpen;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public Room() {
    }

    /**
     * Create a new Room instance.
     */
    public Room(String name) {
        this.name = name;
        this.linkIdStudent = UUID.randomUUID();
        this.linkIdModerator = UUID.randomUUID();
        this.isOpen = true;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getLinkIdStudent() {
        return linkIdStudent;
    }

    public void newLinkIdStudent() {
        this.linkIdStudent = UUID.randomUUID();
    }

    public UUID getLinkIdModerator() {
        return linkIdModerator;
    }

    public void setLinkIdModerator() {
        this.linkIdModerator = UUID.randomUUID();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
