package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.QuestionsUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface QuestionsUpdateRepository extends JpaRepository<QuestionsUpdate, Long> {
    @Query("SELECT u from QuestionsUpdate u where u.user.id=?1 and u.room.id=?2")
    List<QuestionsUpdate> findUpdate(Long idUser, Long idRoom);

    @Transactional
    @Modifying
    @Query("delete from QuestionsUpdate u where u.id=?1 and u.user.id=?2 and u.room.id=?3")
    void deleteUpdate(Long updateId, Long idUser, Long idRoom);
}
