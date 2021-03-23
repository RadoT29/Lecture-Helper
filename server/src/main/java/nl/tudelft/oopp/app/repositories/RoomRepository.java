package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository("RoomRepository")

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE Room r SET r.isOpen=false, r.endDate=?2 WHERE r.id=?1")
    void closeRoom(Long id, LocalDateTime endDate);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Room r SET r.permission=false WHERE r.linkIdModerator=?1")
    void kickAllStudents(UUID link);

    @Modifying
    @Transactional
    @Query("UPDATE Room r SET r.isOpen=true, r.permission=true WHERE r.id=?1")
    void openRoom(long roomId);

    @Query(value = "select r from Room r where r.linkIdModerator=?1 OR r.linkIdStudent=?1")
    Room isClose(UUID link);

    @Query(value = "select r from Room r where r.linkIdModerator=?1 OR r.linkIdStudent=?1")
    Room permission(UUID link);

    @Query("SELECT u FROM Room u WHERE u.linkIdStudent=?1 OR u.linkIdModerator=?1")
    Room findByLink(UUID link);
}
