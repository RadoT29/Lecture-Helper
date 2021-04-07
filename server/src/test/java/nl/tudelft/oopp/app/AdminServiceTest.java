package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.IpAddressRepository;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import nl.tudelft.oopp.app.services.AdminService;
import nl.tudelft.oopp.app.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private IpAddressRepository ipAddressRepository;

    private String realPassword;
    private Room room1;
    private Room room2;
    private List<Room> rooms;
    private String room1Id;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        realPassword = adminService.getRealPassword();
        room1 = new Room("room1 name");
        room1Id = String.valueOf(room1.getId());

        room2 = new Room("room2 name");

        rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);
        when(roomRepository.findAll()).thenReturn(rooms);


    }

    @Test
    void checkAdminPasswordTestTrue() {
        assertTrue(adminService.checkAdminPassword(realPassword));

    }

    @Test
    void checkAdminPasswordTestFalse() {
        assertFalse(adminService.checkAdminPassword(""));

    }

    @Test
    void getRoomsTestEquals() {
        assertEquals(rooms,adminService.getRooms());
    }

    @Test
    void getRoomsTestNotEquals() {
        assertNotEquals(new ArrayList<Room>(),adminService.getRooms());
    }

    @Test
    void unbanAllUsersForRoomTest() {
        adminService.unbanAllUsersForRoom(room1Id);
        verify(ipAddressRepository,times(1))
                .unbanAllUsersForRoom(Long.parseLong(room1Id));
    }




}
