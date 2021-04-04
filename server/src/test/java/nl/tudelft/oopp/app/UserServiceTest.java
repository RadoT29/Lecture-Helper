package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.User;
import nl.tudelft.oopp.app.repositories.IpAddressRepository;
import nl.tudelft.oopp.app.repositories.UserRepository;
import nl.tudelft.oopp.app.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IpAddressRepository ipAddressRepository;

    private User userFirst;

    private User userSecond;

    private List<User> list;

    private Room room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        list = new ArrayList<>();
        room = new Room("myRoom");
        userFirst = new User("First", room);
        userSecond = new User("Second", room);
        list.add(userFirst);
        list.add(userSecond);
    }

    /**
     * Tests if the userService correctly finds all users.
     */
    @Test
    public void findAllTest() {
        when(userRepository.findAll())
                .thenReturn(Arrays.asList(userFirst, userSecond));
        assertEquals(list, userService.findAll());
    }

    /**
     * Tests whether the userService returns the correct number of users.
     */
    @Test
    public void countAllTest() {
        when(userRepository.count())
                .thenReturn(Long.valueOf(list.size()));
        assertEquals(2,userService.count());
    }

    /**
     * Checks if the userService calls the corresponding method
     * in the userRepository.
     */
    @Test
    public void updateTest() {
        userService.update(userFirst.getId(), "Changed");
        verify(userRepository, times(1))
                .updateUserName(userFirst.getId(), "Changed");
    }

    /**
     * Tests the getByID method in a situation
     * in which a user is not found.
     */
    @Test
    public void getByIDTestEmpty() {
        when(userRepository.findById(anyLong()))
        .thenReturn(Optional.empty());

        assertNull(userService.getByID(String.valueOf(userFirst.getId())));
    }

    /**
     * Tests the getByID method in a situation
     * in which a user exists and is found.
     */
    @Test
    public void getByIDTestFound() {
        when(userRepository.findById(userFirst.getId()))
                .thenReturn(Optional.of(userFirst));

        assertEquals(
                userFirst, userService.getByID(String.valueOf(userFirst.getId())));
    }

    /**
     * Verifies that saving the user by ip calls the
     * corresponding method in the repository.
     */
    @Test
    public void saveStudentIpTest() {
        userService.saveStudentIp("1", userFirst, room);
        verify(ipAddressRepository, times(1))
                .save(any());
    }

    /**
     * Verifies that service method calls
     * the appropriate repository method.
     */
    @Test
    public void banUserForThatRoomTest() {
        userService.banUserForThatRoom(String.valueOf(userFirst.getId()),
                String.valueOf(room.getId()));
        verify(ipAddressRepository, times(1))
                .banUserForRoom(room.getId(), userFirst.getId());
    }

    /**
     * Tests whether the userService calls the correct corresponding method
     * in the repository and evaluates the result.
     */
    @Test
    public void isUserBannedTest() {
        when(ipAddressRepository.checkForIpBan("1", room.getId()))
                .thenReturn(Collections.singletonList(1));
        List<Integer> status = new ArrayList<>();
        status.add(1);
        assertEquals(status, userService.isUserBanned("1", room.getId()));
    }

    /**
     * Verifies that service method calls
     * the appropriate repository method.
     */
    @Test
    public void warnUserForThatRoomTest() {
        userService.warnUserForThatRoom(String.valueOf(userSecond.getId()),
                String.valueOf(room.getId()));
        verify(ipAddressRepository, times(1))
                .warnUserForRoom(room.getId(), userSecond.getId());
    }

    /**
     * Tests whether the userService calls the correct corresponding method
     * in the repository and evaluates the result.
     */
    @Test
    public void isUserWarnedTest() {
        when(ipAddressRepository.checkForIpWarn("2", room.getId()))
                .thenReturn(Collections.singletonList(0));
        List<Integer> status = new ArrayList<>();
        status.add(0);
        assertEquals(status, userService.isUserWarned("2", room.getId()));
    }

}
