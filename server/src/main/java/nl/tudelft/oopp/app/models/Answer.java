package nl.tudelft.oopp.app.models;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;


import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor

@Entity
public class Answer {

    @Column(nullable = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String answerText;
    private boolean answeredInClass;

    @ManyToOne
    private Question question;

    @ManyToOne
    private Moderator moderator;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;


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
