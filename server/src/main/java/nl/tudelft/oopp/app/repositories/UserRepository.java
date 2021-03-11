package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
