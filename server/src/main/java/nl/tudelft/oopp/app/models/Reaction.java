package nl.tudelft.oopp.app.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor

@Entity
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int value;
    @ManyToOne
    private Room room;
    @ManyToOne
    private User user;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;


    /**
     * This constructor is used for testing purposes.
     * @param value the value of the reaction
     * @param room the room associated to the reaction
     * @param user the user associated to the reaction
     */
    public Reaction(int value, Room room, User user) {
        this.value = value;
        this.room = room;
        this.user = user;
    }

    public Reaction(int value) {
        this.value = value;
    }
}



