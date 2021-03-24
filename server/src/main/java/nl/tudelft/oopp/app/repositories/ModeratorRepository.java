package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("ModeratorRepository")

public interface ModeratorRepository extends JpaRepository<Moderator, Long> {
}
