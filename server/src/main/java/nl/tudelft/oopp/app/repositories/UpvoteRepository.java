package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Upvote;
import nl.tudelft.oopp.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository("UpvoteRepository")

public interface UpvoteRepository extends JpaRepository<Upvote, Long> {

    @Query("SELECT u FROM Upvote u WHERE u.user.id=?1 AND u.question.id=?2")
    Upvote findUpvoteByUserAndQuestion(long user, long question);

    @Transactional
    @Modifying
    @Query("DELETE FROM Upvote u WHERE u.question.id=?1")
    void deleteUpVotesByQuestionId(long questionId);


    @Transactional
    @Modifying
    @Query("UPDATE Question u SET u.upVotes=u.upVotes+1 WHERE u.id=?1")
    void incrementUpVotes(long questionId);

    @Transactional
    @Modifying
    @Query("UPDATE Question u SET u.upVotes=u.upVotes-1 WHERE u.id=?1")
    void decrementUpVotes(long questionId);

    /*@Transactional
    @Modifying
    @Query("DELETE FROM Upvote u JOIN Question q ON u.Question.id = q.id WHERE q.Room.id=?1" )
    void clearAllUpVotes(long roomId);*/

    @Query("SELECT u.id FROM Upvote u WHERE u.question.id=?1 AND u.question.room.id=?2 "
            + "AND u.user.isModerator= 'true'")
    List<Long> getModUpVotes(long questionId, long roomId, long userId);

}
