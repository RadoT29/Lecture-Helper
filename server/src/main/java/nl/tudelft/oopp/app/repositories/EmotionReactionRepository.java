package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.EmotionReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface EmotionReactionRepository extends JpaRepository<EmotionReaction, Long> {

    @Query("SELECT MAX(u.id) FROM EmotionReaction u WHERE u.room.id=?1 AND u.user.id=?2")
    Long getReactionIdByRoomAndUserIds(long roomId, long userId);

    @Transactional
    @Modifying
    @Query("UPDATE EmotionReaction u SET u.value=?2 WHERE u.id=?1")
    void updateValue(long reactionId, int value);

    @Query("SELECT SUM(u.value) FROM EmotionReaction u WHERE u.room.id=?1")
    int getEmotionSum(long roomId);

    @Query("SELECT COUNT(u.value) FROM EmotionReaction u WHERE u.room.id=?1")
    int getEmotionCount(long roomId);
}
