package nl.tudelft.oopp.app.models;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;


import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor

@Entity
public class Answer {

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

    private String answerText;
    private boolean answeredInClass;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Moderator moderator;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    private String duration;


    /**
     * Constructor for the Answer.
     * @param answerText - text for the answer
     * @param question - question answered
     * @param moderator - moderator who answered
     * @param answeredInClass - if it was answered orally (true)
     *                        or by comments (false)
     */
    public Answer(String answerText,
                  Question question,
                  Moderator moderator,
                  boolean answeredInClass) {
        this.answerText = answerText;
        this.question = question;
        this.moderator = moderator;
        this.answeredInClass = answeredInClass;
    }
    


}
