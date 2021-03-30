package nl.tudelft.oopp.app.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Duration;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor

@Entity
public class PollAnswer {

    @Id
    @SequenceGenerator(
            name = "poll_answer_sequence",
            sequenceName = "poll_answer_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "poll_answer_sequence"
    )
    private long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private PollOption pollOption;

    private boolean isMarked;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public PollAnswer(Student student, PollOption pollOption, boolean isMarked) {
        this.student = student;
        this.pollOption = pollOption;
        this.isMarked = isMarked;
    }

}
