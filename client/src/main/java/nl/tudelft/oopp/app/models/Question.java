package nl.tudelft.oopp.app.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Question implements Comparable<Question> {

    public String questionID;
    public Room room;
    public User user;
    public String questionText;
    public List<Upvote> upvotes;
    //  public LocalDateTime createdAt;
    //  public LocalDateTime updatedAt;


    public Question(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionID() {
        return questionID;
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

    public List<Upvote> getUpvotes() {
        return upvotes;
    }
    public int getNumberOfUpvotes() {
        return upvotes.size();
    }

    @Override
    public int compareTo(Question o) {
        return o.getNumberOfUpvotes() - this.getNumberOfUpvotes();
    }
}
