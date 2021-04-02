package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Repository("RoomRepository")

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE Room r SET r.isOpen=false, r.endDateForStudents=?2 WHERE r.id=?1")
    void closeRoomStudents(Long id, LocalDateTime endDate);

    @Modifying
    @Transactional
    @Query("UPDATE Room r SET r.permission=true, r.isOpen = true  WHERE r.id=?1")
    void openRoomForStudents(long roomId);

    @Query("SELECT u FROM Room u WHERE u.linkIdStudent=?1 OR u.linkIdModerator=?1")
    Room findByLink(UUID link);

    @Query("SELECT r.createdAt FROM Room r WHERE r.id=?1")
    LocalDateTime getRoomTime(long l);

    @Query("SELECT r.updatedAt FROM Room r WHERE r.id=?1")
    Date getUpdatedTime(long l);

    @Query("SELECT r.name FROM Room r WHERE r.id=?1")
    String getRoomName(long roomId);


    String queryValue = "UPDATE Room r Set r.numberQuestionsInterval=?2,r.timeInterval=?3 "
            + "where r.linkIdStudent=?1 or r.linkIdModerator=?1";

    @Modifying
    @Transactional
    @Query(queryValue)
    void putConstraints(UUID roomLink, int numQuestions, int minutes);
}
