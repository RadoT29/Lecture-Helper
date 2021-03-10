package nl.tudelft.oopp.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.User;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import nl.tudelft.oopp.app.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserTest {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveAndRetrieveUser() {
        Room room = new Room("room name");
        roomRepository.save(room);

        User user = new User("Rado",room);
        userRepository.save(user);

        User user2 = userRepository.getOne((long) 1);
        assertEquals(user, user2);
    }

    @Test
    public void saveAndRetrieveRoomViaUser() {
        Room room = new Room("room name");
        roomRepository.save(room);

        User user = new User("Pedro",room);
        userRepository.save(user);

        User user2 = userRepository.getOne((long) 2);
        assertEquals(user2.getRoomId(), room);
    }
}