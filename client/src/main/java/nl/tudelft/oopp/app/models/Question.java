package nl.tudelft.oopp.app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor

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


    @Override
    public int compareTo(Question o) {
        return o.getUpVotes() - this.getUpVotes();
    }
}
