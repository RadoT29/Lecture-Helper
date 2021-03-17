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


    //    public String getName() {
    //        return name;
    //    }
    //
    //    public void setName(String name) {
    //        this.name = name;
    //    }
    //
    //    public Room getRoomId() {
    //        return roomId;
    //    }
    //
    //    public void setRoomId(Room roomId) {
    //        this.roomId = roomId;
    //    }
    //
    //    public long getId() {
    //        return id;
    //    }
    //
    //    public void setId(long id) {
    //        this.id = id;
    //    }
    //
    //    public Date getCreatedAt() {
    //        return createdAt;
    //    }
    //
    //    public Date getUpdatedAt() {
    //        return updatedAt;
    //    }
    //
    //    public Boolean getIsModerator() {
    //        return isModerator;
    //    }
    //
    //    public void setIsModerator(Boolean moderator) {
    //        isModerator = moderator;
    //    }
    //
    //    /**
    //     * The equals method checks whether our user object
    //     * is the same as another object.
    //     * @param other - the object we check.
    //     * @return true if the objects are equal and false otherwise.
    //     */
    //    public boolean equals(Object other) {
    //        if (this == other) {
    //            return true;
    //        }
    //        if (other instanceof User) {
    //            User that = (User) other;
    //            if (this.getRoomId().equals(that.getRoomId())
    //                    && this.getName().equals(that.getName())) {
    //                return true;
    //            }
    //        }
    //
    //        return false;
    //    }
}
