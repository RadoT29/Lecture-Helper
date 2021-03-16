package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Student;
import nl.tudelft.oopp.app.models.User;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import nl.tudelft.oopp.app.repositories.UserRepository;
import nl.tudelft.oopp.app.services.RoomService;
import nl.tudelft.oopp.app.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class ModeratorStudentTestMock {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void SetUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Find and return the user by id.
     */
    @Test
    public void getUserById() {
        Room room = new Room("My room");
        User user = new Student("Tom", room);
        when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));
        assertEquals(user, userService.getByID(String.valueOf(user.getId())));
    }

}
