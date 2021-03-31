package nl.tudelft.oopp.app.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor

@Entity
public class PollOption {

    @Id
    @SequenceGenerator(
            name = "poll_option_sequence",
            sequenceName = "poll_option_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.IDENTITY,
            generator = "poll_option_sequence"
    )
    private long id;

    @Getter(value = AccessLevel.NONE)
    @ManyToOne
    private Poll poll;

    private String optionText;

    private boolean isCorrect;

    @Transient
    private double scoreRate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public PollOption(Poll poll, String optionText, boolean isCorrect) {
        this.poll = poll;
        this.optionText = optionText;
        this.isCorrect = isCorrect;
    }

}
