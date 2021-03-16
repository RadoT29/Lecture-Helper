package nl.tudelft.oopp.app.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Question implements Comparable<Question> {

    public long id;
    public Room room;
    public User user;
    public String questionText;
    public int upVotes;
    //  public LocalDateTime createdAt;
    //  public LocalDateTime updatedAt;


    public Question(String questionText) {
        this.questionText = questionText;
    }

    public long getQuestionID() {
        return id;
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



    public int getUpVotes() {
        return upVotes;
    }

    @Override
    public int compareTo(Question o) {
        return o.getUpVotes() - this.getUpVotes();
    }
}
