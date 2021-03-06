package nl.tudelft.oopp.app.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * This class represents an user of the application.
 * The user is stored in the database.
 */
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room roomId;


    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private  Date updatedAt;

    public User(String name, Room roomId) {
        this.name = name;
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room getRoomId() {
        return roomId;
    }

    public void setRoomId(Room roomId) {
        this.roomId = roomId;
    }

    public long getId() {
        return id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public boolean equals(Object other){
        if(this == other){
            return true;
        }
        if(other instanceof User){
            User that = (User) other;
            if(this.getRoomId().equals(that.getRoomId()) &&
            this.getName().equals(that.getName())){
                return true;
            }
        }

        return false;
    }
}
