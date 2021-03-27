package nl.tudelft.oopp.app.models;

import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor

public class QuestionsUpdate {
    private long id;
    private User user;
    private Room room;
    private String questionText;
    private String answerText;
    private int statusQuestion;


}
