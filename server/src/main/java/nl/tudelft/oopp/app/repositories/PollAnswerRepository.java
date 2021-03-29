package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.PollAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("PollAnswerRepository")
public interface PollAnswerRepository extends JpaRepository<PollAnswer, Long> {
}
