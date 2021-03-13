package nl.tudelft.oopp.app.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class Question {

    public String questionID;
    public String roomId;
    public String userId;
    public String questionText;
    //  public LocalDateTime createdAt;
    //  public LocalDateTime updatedAt;


    public Question(String questionText) {
        this.questionText = questionText;
    }

    public String getQuestionID() {
        return questionID;
    }
}
