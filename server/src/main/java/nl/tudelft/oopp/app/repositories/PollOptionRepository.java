package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository("PollOptionRepository")
public interface PollOptionRepository extends JpaRepository<PollOption, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM PollOption po WHERE po.poll.id=?1")
    void deleteOptionsFromRoom(long pollId);
}
