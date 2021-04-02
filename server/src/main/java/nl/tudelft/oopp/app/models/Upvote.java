package nl.tudelft.oopp.app.models;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor

@Entity
public class Upvote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The onDelete feature establishes that once a question is deleted
     * All of its corresponding entries in the upVotes table are also deleted.
     */
    @Getter(value = AccessLevel.NONE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    public Question question;

    @ManyToOne
    public User user;

    public Boolean isModerator;

    public Integer value = 0;

    /**
     * Constructor for upvote.
     * @param question - question
     * @param user - user
     */
    public Upvote(Question question, User user) {
        this.question = question;
        this.user = user;
        this.isModerator = user.getIsModerator();
    }

}
