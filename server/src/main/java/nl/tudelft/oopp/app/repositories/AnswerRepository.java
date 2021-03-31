package nl.tudelft.oopp.app.repositories;


import nl.tudelft.oopp.app.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Answer u WHERE u.question.id=?1")
    void deleteByQuestionID(long questionId);


    @Query("SELECT u.id FROM Answer u WHERE u.question.id=?1")
    List<Long> getAllAnswerIds(long roomId);

    @Query("SELECT u.answerText FROM Answer u WHERE u.id=?1")
    String getAnswerText(long questionId);

    @Query("SELECT u.createdAt FROM Answer u WHERE u.id=?1")
    LocalDateTime getAnswerTime(long questionId);


}
