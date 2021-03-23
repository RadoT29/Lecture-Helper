package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Clock;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class RoomTest {
    @Autowired
    private RoomRepository roomRepository;

    /**
     * check if the created room is with the same name.
     */
    @Test
    public void roomName() {
        Room room = new Room("room name");
        assertEquals(room.getName(), "room name");
    }

    /**
     * check if the moderator link is created.
     */
    @Test
    public void roomLinksModerator() {
        Room room = new Room("room name");
        assertNotNull(room.getLinkIdModerator());
    }

    /**
     * check if the student link is created.
     */
    @Test
    public void roomLinksStudent() {
        Room room = new Room("room name");
        assertNotNull(room.getLinkIdStudent());
    }

    /**
     * check if the room saved and after that retrieved from the repository is the same.
     */
    @Test
    public void saveAndRetrieveRoom() {
        Room room = new Room("room name");
        roomRepository.save(room);

        Room room2 = roomRepository.getOne(room.getId());
        assertEquals(room, room2);
    }


    /**
     * Check if the other room on the repository is still open.
     */
    @Test
    public void closeRoomFalse() {
        Room room = new Room("My room");
        Room room2 = new Room("New room");
        roomRepository.save(room);
        roomRepository.save(room2);
        roomRepository.closeRoom(room.getId(), LocalDateTime.now(Clock.systemUTC()));
        assertEquals(true, room2.getIsOpen());
    }

}
