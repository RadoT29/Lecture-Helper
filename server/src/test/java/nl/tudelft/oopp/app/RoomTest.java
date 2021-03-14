package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class RoomTest {
    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void roomName() {
        Room room = new Room("room name");
        assertEquals(room.getName(), "room name");
    }

    @Test
    public void roomLinks() {
        Room room = new Room("room name");
        assertNotNull(room.getLinkIdModerator());
        assertNotNull(room.getLinkIdStudent());
    }

    @Test
    public void saveAndRetrieveRoom() {
        Room room = new Room("room name");
        roomRepository.save(room);

        Room room2 = roomRepository.getOne(room.getId());
        assertEquals(room, room2);
    }

    /**
     * Check if the room is close
     */
    @Test
    public void closeRoom(){
        Room room = new Room("My room");
        roomRepository.save(room);
        roomRepository.closeRoom(room.getLinkIdModerator());
        assertEquals(false,room.isOpen());
    }
    /**
     *Check if the the students has permission to room (if they do not have permission so they are kickked)
     */
    @Test
    public void kickTheStudents(){
        Room room = new Room("My room");
        roomRepository.save(room);
        roomRepository.kickAllStudents(room.getLinkIdModerator());
        assertEquals(false,room.getPermission());
    }
    /**
     *Check if the other room on the repository is still open
     */
    @Test
    public void closeRoomFalse(){
        Room room = new Room("My room");
        Room room2 = new Room("New room");
        roomRepository.save(room);
        roomRepository.save(room2);
        roomRepository.closeRoom(room.getLinkIdModerator());
        assertEquals(true,room2.isOpen());
    }
    /**
     *Check if the other room is still open for students
     */
    @Test
    public void kickTheStudentsFalse(){
        Room room = new Room("My room");
        Room room2 = new Room("New room");
        roomRepository.save(room);
        roomRepository.save(room2);
        roomRepository.kickAllStudents(room.getLinkIdModerator());
        assertEquals(true,room2.getPermission());
    }
}
