package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("SELECT u FROM Question u WHERE (u.room.linkIdStudent=?1 OR u.room.linkIdModerator=?1)"
            + " AND u.answered='false'")
    List<Question> findAllByRoomLink(UUID link);

    @Transactional
    @Modifying
    @Query("DELETE FROM Question u WHERE u.id=?1 AND u.user.id=?2")
    void deleteSingular(long questionId, long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Question u WHERE u.room.id=?1")
    void clearQuestions(long roomId);

    @Transactional
    @Modifying
    @Query("UPDATE Question u SET u.questionText=?2 WHERE u.id=?1")
    void editQuestionText(long questionId, String newText);

    @Query("SELECT MAX(u.id) FROM Question u WHERE u.room.id=?1 AND u.user.id=?2")
    String getSingularQuestion(long roomId, long userId);

    @Query("SELECT u.answered FROM Question u WHERE u.id=?1 AND u.room.id=?2")
    boolean checkAnswered(long questionId, long roomId);

    @Transactional
    @Modifying
    @Query("UPDATE Question u SET u.answered=?2 WHERE u.id=?1")
    void updateAnswerStatus(long questionId, boolean status);

    @Query("SELECT u.id FROM Question u WHERE u.room.id=?1")
    List<Long> getAllQuestionIds(long roomId);




}
