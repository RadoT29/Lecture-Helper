package nl.tudelft.oopp.app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.controllers.HomeSceneController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
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
    public String duration;
    public String ageSeconds;
    public int upVotesFinal;

    //  public LocalDateTime createdAt;
    //  public LocalDateTime updatedAt;


    public Question(String questionText) {
        this.questionText = questionText;
    }

    /**
     * The compareTo method takes into account the upvotes total and the time of the question
     * If a question that is more recent gets the same amount of upvotes as an older question
     * It will be more likely to show up at the top than the older question
     * This because it will get a higher amount of points when compared to the other question
     * (as the last one is older).
     * @param o - Question to be compared
     * @return int value of comparator
     */
    @Override
    public int compareTo(Question o) {
        int extra = extraPoints(this);
        int extra1 = extraPoints(o);

        return ((o.getUpVotesFinal() + extra1) - (this.getUpVotesFinal() + extra));
    }

    /**
     * Method to attribute the extra points to recent questions
     * the age of a question is in seconds, meaning that as time goes by it will get less
     * extra points and its relevance will mostly be by upvotes and not time
     * To avoid misallocating extra points, questions with less than 5 upvotes are not considered.
     * @param o - question to attribute the points
     * @return int - amount of points
     */
    public int extraPoints(Question o) {
        if (o.ageSeconds == null || o.ageSeconds.equals("0") || o.getUpVotesFinal() < 5) {
            return 0;
        }

        int age1 = Integer.parseInt(o.ageSeconds);
        return  ((60 + age1) / (age1));
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
        return Objects.equals(room, question.room)
                && Objects.equals(user, question.user)
                && Objects.equals(questionText, question.questionText);
    }

}
