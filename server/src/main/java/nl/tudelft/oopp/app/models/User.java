package nl.tudelft.oopp.app.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private Boolean isModerator;

    @ManyToOne
    private Room roomId;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    private String timeZone;


    /**
     * Create a user.
     *
     * @param name   - name of user
     * @param roomId - the room, which the user enters.
     */
    public User(String name, Room roomId) {
        this.name = name;
        this.roomId = roomId;
    }

    /**
     * Create a user with a roomId and role.
     *
     * @param roomId      - the room, which the user enters.
     * @param isModerator - true if Moderator, false if Student
     */
    public User(Room roomId, boolean isModerator) {
        this.roomId = roomId;
        this.name = "Anonymous";
        this.isModerator = isModerator;
    }

}
