package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("SELECT u FROM Feedback u WHERE u.room.id=?1")
    List<Feedback> findAllByRoomId(long roomId);
}
