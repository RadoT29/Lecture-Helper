package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Reaction;
import nl.tudelft.oopp.app.models.SpeedReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface SpeedReactionRepository extends JpaRepository<SpeedReaction, Long> {

    @Query("SELECT MAX(u.id) FROM SpeedReaction u WHERE u.room.id=?1 AND u.user.id=?2")
    Long getReactionIdByRoomAndUserIds(long roomId, long userId);

    @Transactional
    @Modifying
    @Query("UPDATE SpeedReaction u SET u.value=?2 WHERE u.id=?1")
    void updateValue(long reactionId, int value);

    @Query("SELECT SUM(u.value) FROM SpeedReaction u WHERE u.room.id=?1")
    int getSpeedSum(long roomId);

    @Query("SELECT COUNT(u.value) FROM SpeedReaction u WHERE u.room.id=?1")
    int getSpeedCount(long roomId);
}
