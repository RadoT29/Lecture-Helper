package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE Room r SET r.isOpen=false WHERE r.name='My room'")
    void closeRoom(String name);

}
