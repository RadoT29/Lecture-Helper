package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
@Repository("RoomRepository")
public interface RoomRepository extends JpaRepository<Room, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE Room r SET r.isOpen=false WHERE r.name=?1")
    void closeRoom(String name);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Room r SET r.permission=false WHERE r.name=?1")
    void kickAllStudents(String name);

//    String query = "UPDATE Student s " +
//            "SET s.permission = false " +
//            "FROM s " +
//            "JOIN Room as r " +
//            "On s.roomID=r.id " +
//            "WHERE r.name=?1";
//
//    @Modifying
//    @Transactional
//    @Query(value = query)
//    void kickAllStudent(String name);
}
