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
}