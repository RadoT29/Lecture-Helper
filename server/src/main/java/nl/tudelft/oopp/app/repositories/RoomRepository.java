package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository("RoomRepository")

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE Room r SET r.isOpen=false WHERE r.linkIdModerator=?1")
    void closeRoom(UUID link);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Room r SET r.permission=false WHERE r.linkIdModerator=?1")
    void kickAllStudents(UUID link);

    @Query(value = "select r from Room r where r.linkIdModerator=?1 OR r.linkIdStudent=?1")
    Room isClose(UUID link);

    @Query(value = "select r from Room r where r.linkIdModerator=?1 OR r.linkIdStudent=?1")
    Room permission(UUID link);

    @Query("SELECT u FROM Room u WHERE u.linkIdStudent=?1 OR u.linkIdModerator=?1")
    Room findByLink(UUID link);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Room r Set r.numberQuestionsInterval=?2, r.timeInterval=?3 where r.linkIdStudent=?1 or r.linkIdModerator=?1")
    void putConstraints(UUID roomLink, int numQuestions, int minutes);
}
