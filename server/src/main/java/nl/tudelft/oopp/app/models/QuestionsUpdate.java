package nl.tudelft.oopp.app.models;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor

@Entity
public class QuestionsUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Room room;

//    @ManyToOne
//    private Question question;
//
//    @ManyToOne
//    private Answer answer;

    private String questionText;

    private String answerText;

    //status types:
    // -1 question discarded
    // 0 question marked as answered during the lecture
    // 1 question answered in the room
    private int statusQuestion;



    public QuestionsUpdate(User user, Room room, int statusQuestion) {
        this.user = user;
        this.room = room;
//        this.question = null;
//        this.answer = null;
        this.statusQuestion = statusQuestion;
        this.questionText = "";
        this.answerText = "";
    }
}
