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

    @Query("SELECT u FROM Question u WHERE u.room.linkIdStudent=?1 OR u.room.linkIdModerator=?1")
    List<Question> findAllByRoomLink(UUID link);

    @Transactional
    @Modifying
    @Query("DELETE FROM Question u WHERE u.room.id=?1")
    void clearQuestions(long roomId);


}