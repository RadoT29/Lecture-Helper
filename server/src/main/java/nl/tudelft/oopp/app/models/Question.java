package nl.tudelft.oopp.app.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


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

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Question() {
    }

    public Question(String questionText) {
        this.questionText = questionText;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public User getUser() {
        return user;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        return id == question.id
                && Objects.equals(room, question.room)
                && Objects.equals(user, question.user)
                && Objects.equals(questionText, question.questionText)
                && Objects.equals(createdAt, question.createdAt)
                && Objects.equals(updatedAt, question.updatedAt)
                && Objects.equals(upVotes, question.upVotes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, room, user, questionText, createdAt, updatedAt);
    }
}
