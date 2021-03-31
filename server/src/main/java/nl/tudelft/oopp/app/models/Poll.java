package nl.tudelft.oopp.app.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor

@Entity
public class Poll {

    @Id
    @SequenceGenerator(
            name = "poll_sequence",
            sequenceName = "poll_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "poll_sequence"
    )
    private long id;

    @ManyToOne
    private Room room;

    @OneToMany(mappedBy="poll")
    private List<PollOption> pollOptions;

    private String question;

    private boolean isOpen = false;

    private boolean isFinished = false;

    private Duration timeLimit = null;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Poll(Room room) {
        this.room = room;
    }

    public Poll(String question, Room room) {
        this.question = question;
        this.room = room;
    }

    public Poll(String question, Room room, Duration timeLimit) {
        this.question = question;
        this.room = room;
        this.timeLimit = timeLimit;
    }

}
