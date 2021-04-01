package nl.tudelft.oopp.app.models;

import lombok.*;
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

    private int numberQuestionsInterval = Integer.MAX_VALUE;

    private int timeInterval = Integer.MAX_VALUE;

    private LocalDateTime startDate;

    private LocalDateTime endDateForStudents;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;


    /**
     * Create a new Room instance.
     */
    public Room(String name) {
        this.name = name;
        this.linkIdStudent = UUID.randomUUID();
        this.linkIdModerator = UUID.randomUUID();
        this.isOpen = true;
        this.permission = true;
        this.startDate = LocalDateTime.now(Clock.systemUTC());
    }

    /**
     * Create a new Room instance (for creating room in advance).
     */
    public Room(String name, LocalDateTime startDate) {
        this.name = name;
        this.linkIdStudent = UUID.randomUUID();
        this.linkIdModerator = UUID.randomUUID();
        this.isOpen = true;
        this.permission = false;
        this.startDate = startDate;
    }

    public void setLinkIdModerator() {
        this.linkIdModerator = UUID.randomUUID();
    }

    public void newLinkIdStudent() {
        this.linkIdStudent = UUID.randomUUID();
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

}
