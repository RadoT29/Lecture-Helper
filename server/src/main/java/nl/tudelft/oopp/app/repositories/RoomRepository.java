package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {}
