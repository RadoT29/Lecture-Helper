package nl.tudelft.oopp.app.repositories;


import nl.tudelft.oopp.app.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Answer u WHERE u.question.id=?1")
    void deleteByQuestionID(long questionId);

}
