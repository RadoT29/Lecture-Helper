package nl.tudelft.oopp.app.repositories;


import nl.tudelft.oopp.app.models.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
}
