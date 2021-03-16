package nl.tudelft.oopp.app.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class Upvote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The onDelete feature establishes that once a question is deleted
     * All of its corresponding entries in the upVotes table are also deleted.
     */
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    public Question question;

    @ManyToOne
    public User user;

    public Upvote(Question question, User user) {
        this.question = question;
        this.user = user;
    }

    public Upvote() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
