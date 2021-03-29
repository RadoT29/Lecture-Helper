package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Poll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("PollRepository")
public interface PollRepository extends JpaRepository<Poll, Long> {
}
