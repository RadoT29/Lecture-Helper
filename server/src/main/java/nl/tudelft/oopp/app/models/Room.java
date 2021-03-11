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
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private UUID linkIdStudent;

    private UUID linkIdModerator;

    private boolean isOpen;

    @Column(name = "student_permission")
    private boolean permission;
    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
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

    public boolean getPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }
}
