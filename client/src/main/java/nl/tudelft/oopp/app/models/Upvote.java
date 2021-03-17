package nl.tudelft.oopp.app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class Upvote {
    private long id;
    private String questionId;
    private User user;

    public Upvote(String questionId, User user) {
        this.questionId = questionId;
        this.user = user;
    }

//    public long getUpvoteId() {
//        return id;
//    }
//
//    public String getQuestionId() {
//        return questionId;
//    }
//
//    public User getUser() {
//        return user;
//    }
}
