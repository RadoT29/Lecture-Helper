package nl.tudelft.oopp.app.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor

@Entity
public class Question {

    @Id
    @SequenceGenerator(
            name = "question_sequence",
            sequenceName = "question_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "question_sequence"
    )
    private long id;
    private int upVotes;
    @ManyToOne
    private Room room;
    @ManyToOne
    private User user;

    private String questionText;

    private String answerText;

    private boolean answered;
    
    //corresponds to the time when it was created after start of the room
    private String duration;

    //corresponds to time since its creation
    private String ageSeconds;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Integer totalUpVotes = 0;
    
    public Question(String questionText) {
        this.questionText = questionText;
    }

}
