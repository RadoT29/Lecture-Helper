package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Poll;
import nl.tudelft.oopp.app.models.PollAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository("PollAnswerRepository")
public interface PollAnswerRepository extends JpaRepository<PollAnswer, Long> {
    @Query("SELECT p FROM PollAnswer p WHERE (p.pollOption.id=?1 and p.student.id=?2)")
    PollAnswer findAnswerByUserAndOptionId(long pollOptionId, long userId);

    @Transactional
    @Modifying
    @Query("UPDATE PollAnswer p SET p.isMarked=?2 WHERE p.id=?1")
    void updateAnswer(long id, boolean isMarked);

    @Query("SELECT p FROM PollAnswer p WHERE (p.pollOption.poll.id=?1 and p.student.id=?2)")
    List<PollAnswer> findAnswersByUserAndPollId(long pollId, long userId);

    @Query("SELECT count(p) FROM PollAnswer p WHERE (p.pollOption.id=?1)")
    int countByPollOptionId(long pollOptionId);

    @Query("SELECT count(p) FROM PollAnswer p WHERE (p.pollOption.id=?1 and p.isMarked=true)")
    int countMarkedByPollOptionId(long pollOptionId);
}