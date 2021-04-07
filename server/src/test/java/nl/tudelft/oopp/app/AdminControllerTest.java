package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.controllers.AdminController;
import nl.tudelft.oopp.app.models.Room;
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

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class AdminControllerTest {

    @InjectMocks
    AdminController adminController;

    @Mock
    private AdminService adminService;
    @Mock
    private RoomService roomService;

    private Room room;
    private final String password = "";
    private String roomLink;
    private String roomId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        room = new Room("room name");
        roomId = String.valueOf(room.getId());
        roomLink = room.getLinkIdModerator() + "";
        when(roomService.getByLink(roomLink)).thenReturn(room);

    }

    /**
     * Tests that the controller calls the right service to check the password.
     */
    @Test
    void checkPasswordTest() {
        adminController.checkPassword(password);
        verify(adminService,times(1)).checkAdminPassword(password);
    }


    /**
     * Tests that the controller checks the password and then call the right service method.
     */
    @Test
    void getRoomsTestCorrectPassword() {
        when(adminService.checkAdminPassword(password)).thenReturn(true);
        adminController.getRooms(password);
        verify(adminService,times(1)).getRooms();
    }


    /**
     * Tests that the controller checks the password and returns since the password is wrong.
     */
    @Test
    void getRoomsTestWrongPassword() {
        when(adminService.checkAdminPassword(password)).thenReturn(false);
        assertNull(adminController.getRooms(password));
        verify(adminService,times(0)).getRooms();
    }

    /**
     * Tests that the controller checks the password and then calls the right service method.
     */
    @Test
    void unbanAllUsersTestCorrectPassword() {

        when(adminService.checkAdminPassword(password)).thenReturn(true);
        adminController.unbanAllUsersForThatRoom(password, roomLink);
        verify(adminService,times(1)).unbanAllUsersForRoom(roomId);
    }

    /**
     * Tests that the controller checks the password and returns since the password is wrong.
     */
    @Test
    void unbanAllUsersTestWrongPassword() {
        when(adminService.checkAdminPassword(password)).thenReturn(false);
        adminController.unbanAllUsersForThatRoom(password, roomLink);
        verify(adminService,times(0)).unbanAllUsersForRoom(roomId);
    }

}
