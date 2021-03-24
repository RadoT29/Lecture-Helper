package nl.tudelft.oopp.app.models;

public class Answer {

    public long id;
    public String answerText;
    public String userId;
    public String questionId;
    public String createdAt;
    public String updatedAt;
    public String answerType;

    public Answer(String answerText) {
        this.answerText = answerText;
    }




}
