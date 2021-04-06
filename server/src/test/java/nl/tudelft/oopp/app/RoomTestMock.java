package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.controllers.RoomController;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import nl.tudelft.oopp.app.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class RoomTestMock {

    @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository repository;

    @InjectMocks
    private RoomController roomController;

    @Mock
    private RoomService getRoomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    /**
     * Find the room by moderator link.
     * In the test the expected room is different from
     * that is returned from when().thenReturn() structure
     */
    @Test
    public void findRoomByModeratorLinkFalse() {
        Room room = new Room("My room");
        Room room2 = new Room("new Room");
        when(repository.findByLinkModerator(room.getLinkIdModerator())).thenReturn(room);
        assertNotEquals(room2,
                roomService.getByLinkModerator(String.valueOf(room.getLinkIdModerator())));

    }

    /**
     * Find the room by student link.
     * In the test the expected room is different from that is returned from
     * when().thenReturn() structure.
     */
    @Test
    public void findRoomByStudentLinkFalse() {
        Room room = new Room("My room");
        Room room2 = new Room("new Room");
        when(repository.findByLink(room.getLinkIdStudent())).thenReturn(room);
        assertNotEquals(room2, roomService.getByLink(String.valueOf(room.getLinkIdStudent())));

    }

    /**
     * checks if the room is scheduled correctly.
     */
    @Test
    public void scheduleRoomTest() {
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        Room expected = new Room("Room", now);

        when(repository.save(any(Room.class))).thenReturn(expected);
        Room actual = roomService.scheduleRoom("Room", now.toString());

        //we set the same links, because they are generated while creating the object
        expected.setLinkIdModerator(actual.getLinkIdModerator());
        expected.setLinkIdStudent(actual.getLinkIdStudent());

        assertEquals(expected, actual);
    }

    @Test
    public void testIsOpenTrue() {
        Room room = new Room("My room");
        when(getRoomService.getByLink(String.valueOf(room.getLinkIdStudent()))).thenReturn(room);
        assertTrue(roomController.isOpen(String.valueOf(room.getLinkIdStudent())));
    }

    @Test
    public void testIsOpenFalse1() {
        Room room = new Room("My room");
        room.setPermission(false);
        when(getRoomService.getByLink(String.valueOf(room.getLinkIdStudent()))).thenReturn(room);
        assertFalse(roomController.isOpen(String.valueOf(room.getLinkIdStudent())));
    }

    @Test
    public void testIsOpenFalse2() {
        Room room = new Room("My room");
        room.setIsOpen(false);
        when(getRoomService.getByLink(String.valueOf(room.getLinkIdStudent()))).thenReturn(room);
        assertFalse(roomController.isOpen(String.valueOf(room.getLinkIdStudent())));
    }

    @Test
    public void testIsOpenFalse3() {
        Room room = new Room("My room");
        room.setPermission(false);
        room.setIsOpen(false);
        when(getRoomService.getByLink(String.valueOf(room.getLinkIdStudent()))).thenReturn(room);
        assertFalse(roomController.isOpen(String.valueOf(room.getLinkIdStudent())));
    }

    @Test
    public void testKickAllStudentsWithModeratorLink() {
        UUID uuid = UUID.randomUUID();
        Room room = new Room("My room");
        when(repository.findByLinkModerator(room.getLinkIdModerator())).thenReturn(room);
        roomController.kickAllStudent(String.valueOf(room.getLinkIdModerator()));
        verify(repository).closeRoomStudents(room.getId());
    }

    @Test
    public void testKickAllStudentsWithStudentLink() {
        UUID uuid = UUID.randomUUID();
        Room room = new Room("My room");
        when(repository.findByLink(room.getLinkIdStudent())).thenReturn(room);
        roomController.openRoomStudent(String.valueOf(room.getLinkIdStudent()));
        verify(repository, never()).openRoomForStudents(room.getId());
    }

    @Test
    public void testOpenRoomStudentWithModeratorLink() {
        UUID uuid = UUID.randomUUID();
        Room room = new Room("My room");
        when(repository.findByLink(room.getLinkIdModerator())).thenReturn(room);
        roomController.openRoomStudent(String.valueOf(room.getLinkIdModerator()));
        verify(repository).openRoomForStudents(room.getId());
    }

    @Test
    public void testOpenRoomStudentWithStudentLink() {
        UUID uuid = UUID.randomUUID();
        Room room = new Room("My room");
        when(repository.findByLink(room.getLinkIdStudent())).thenReturn(room);
        roomController.openRoomStudent(String.valueOf(room.getLinkIdStudent()));
        verify(repository, never()).openRoomForStudents(room.getId());
    }

    @Test
    public void testPutConstraints() {
        UUID uuid = UUID.randomUUID();
        roomController.putConstraints(uuid.toString(), "3", "3");
        verify(getRoomService).putConstraints(uuid.toString(), 3, 3);
    }

}
