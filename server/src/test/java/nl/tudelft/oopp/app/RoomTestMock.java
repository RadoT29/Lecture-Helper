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

    @Mock
    private RoomService roomServiceMock;

    @InjectMocks
    private RoomController roomController;

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
        when(repository.findByLink(room.getLinkIdModerator())).thenReturn(room);
        assertNotEquals(room2, roomService.getByLink(String.valueOf(room.getLinkIdModerator())));

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
     * check if the room is closed.
     */
    @Test
    public void testMethodIsClose() {
        Room room = new Room("My room");
        when(roomServiceMock.getByLink(room.getLinkIdModerator().toString())).thenReturn(room);
        when(repository.findByLink(room.getLinkIdModerator())).thenReturn(room);
        assertEquals(true, roomController.isClose(String.valueOf(room.getLinkIdModerator())));
    }

    /**
     * check if the room is closed.
     */
    @Test
    public void testMethodIsCloseClosedRoom() {
        Room room = new Room("My room");
        room.setIsOpen(false);
        when(roomServiceMock.getByLink(room.getLinkIdModerator().toString())).thenReturn(room);
        when(repository.findByLink(room.getLinkIdModerator())).thenReturn(room);
        assertEquals(false, roomController.isClose(String.valueOf(room.getLinkIdModerator())));
    }

    /**
     * check if students have permission to the room.
     */
    @Test
    public void testMethodGetPermission() {
        Room room = new Room("My room");
        when(roomServiceMock.getByLink(room.getLinkIdModerator().toString())).thenReturn(room);
        when(repository.findByLink(room.getLinkIdModerator())).thenReturn(room);
        assertEquals(true, roomController.hasStudentPermission(
                String.valueOf(room.getLinkIdModerator())));
    }

    /**
     * check if students have permission to the room.
     */
    @Test
    public void testMethodGetPermissionFalse() {
        Room room = new Room("My room");
        room.setPermission(false);
        when(roomServiceMock.getByLink(room.getLinkIdModerator().toString())).thenReturn(room);
        when(repository.findByLink(room.getLinkIdModerator())).thenReturn(room);
        assertEquals(false, roomController.hasStudentPermission(
                String.valueOf(room.getLinkIdModerator())));
    }

    /**
     * tests if updateIsOpen changes the IsOpen boolean to true when the room should be open.
     */
    @Test
    public void updateIsOpenTrueTest() {
        //new room with startDate in the past
        Room room = new Room("My room", LocalDateTime.now(Clock.systemUTC()).minusHours(1));
        assertFalse(room.getIsOpen());
        doNothing().when(repository).openRoom(room.getId());
        roomService.updateIsOpen(room);
        assertTrue(room.getIsOpen());
    }

    /**
     * tests if updateIsOpen does not change the IsOpen boolean to true
     * when the room should be still closed.
     */
    @Test
    public void updateIsOpenFalseText() {
        //new room with startDate in the future
        Room room = new Room("My room", LocalDateTime.now(Clock.systemUTC()).plusMinutes(1));
        assertFalse(room.getIsOpen());
        doNothing().when(repository).openRoom(room.getId());
        roomService.updateIsOpen(room);
        assertFalse(room.getIsOpen());
    }

    /**
     * tests if updateIsOpen does not reopen the room after it's closed.
     */
    @Test
    public void updateIsOpenText() {
        //new room with startDate in the past
        Room room = new Room("My room", LocalDateTime.now(Clock.systemUTC()));

        //close the room
        room.setEndDate(LocalDateTime.now(Clock.systemUTC()));
        room.setIsOpen(false);

        roomService.updateIsOpen(room);
        assertFalse(room.getIsOpen());
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

}
