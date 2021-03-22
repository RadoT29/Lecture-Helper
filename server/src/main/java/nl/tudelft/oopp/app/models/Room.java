package nl.tudelft.oopp.app.models;

import lombok.*;
import org.apache.tomcat.jni.Local;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private UUID linkIdStudent;

    private UUID linkIdModerator;

    private boolean isOpen;

    private boolean permission;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    //    public Room() {
    //    }

    /**
     * Create a new Room instance.
     */
    public Room(String name) {
        this.name = name;
        this.linkIdStudent = UUID.randomUUID();
        this.linkIdModerator = UUID.randomUUID();
        this.isOpen = true;
        this.startDate = LocalDateTime.now(Clock.systemUTC());
        this.permission = true;
    }

    /**
     * Create a new Room instance (for creating room in advance).
     */
    public Room(String name, LocalDateTime startDate) {
        this.name = name;
        this.linkIdStudent = UUID.randomUUID();
        this.linkIdModerator = UUID.randomUUID();
        this.isOpen = false;
        this.startDate = startDate;
        this.permission = false;
    }

    public void setLinkIdModerator() {
        this.linkIdModerator = UUID.randomUUID();
    }

    public void newLinkIdStudent() {
        this.linkIdStudent = UUID.randomUUID();
    }

    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public boolean getPermission() {
        return permission;
    }

    public void setPermission(boolean permission) {
        this.permission = permission;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
