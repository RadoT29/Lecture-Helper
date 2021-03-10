package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT u FROM Room u WHERE u.linkIdStudent=?1 OR u.linkIdModerator=?1")
    Room findByLink(UUID link);
}
