package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Poll;
import nl.tudelft.oopp.app.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository("PollRepository")
public interface PollRepository extends JpaRepository<Poll, Long> {
    @Query("SELECT p FROM Poll p WHERE (p.room.linkIdStudent=?1 OR p.room.linkIdModerator=?1)")
    List<Poll> findAllByRoomLink(UUID link);

    @Transactional
    @Modifying
    @Query("UPDATE Poll p SET p.question=?2, p.isOpen=true WHERE p.id=?1")
    void updateAndOpenPoll(long id, String question);

    @Transactional
    @Modifying
    @Query("UPDATE Poll p SET p.isOpen=false WHERE p.id=?1")
    void closePoll(long questionId);
}
