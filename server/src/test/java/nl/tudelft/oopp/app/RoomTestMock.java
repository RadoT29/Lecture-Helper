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

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
//@DataJpaTest
public class RoomTestMock {

     @InjectMocks
    private RoomService roomService;

    @Mock
    private RoomRepository repository;

    @BeforeEach
    void SetUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Check the size of the list which is returned from method findAll
     */
    @Test
    public void roomSize()
    {
        when(repository.findAll()).thenReturn(Stream.of(new Room("my room")).collect(Collectors.toList()));
        assertEquals(1,roomService.findAll().size());
        //verify()
    }

    /**
     * check the works of the method count
     */
    @Test
    public void roomCounter(){
        when(repository.count()).thenReturn(5L);
        assertEquals(5,roomService.count());
    }

    /**
     * Find the room by moderator link.
     */
    @Test
    public void findRoomByModeratorLink()
    {
        Room room = new Room("My room");
        when(repository.findByLink(room.getLinkIdModerator())).thenReturn(room);
        assertEquals(room,roomService.getByLink(String.valueOf(room.getLinkIdModerator())));

    }

    /**
     * Find the room by student link.
     */
    @Test
    public void findRoomByStudentLink()
    {
        Room room = new Room("My room");
        when(repository.findByLink(room.getLinkIdStudent())).thenReturn(room);
        assertEquals(room,roomService.getByLink(String.valueOf(room.getLinkIdStudent())));

    }

    /**
     * Find the room by moderator link.
     * In the test the expected room is different from that is returned from when().thenReturn() structure
     */
    @Test
    public void findRoomByModeratorLinkFalse()
    {
        Room room = new Room("My room");
        Room room2 = new Room("new Room");
        when(repository.findByLink(room.getLinkIdModerator())).thenReturn(room);
        assertNotEquals(room2,roomService.getByLink(String.valueOf(room.getLinkIdModerator())));

    }
    /**
     * Find the room by student link.
     * In the test the expected room is different from that is returned from when().thenReturn() structure
     */
    @Test
    public void findRoomByStudentLinkFalse()
    {
        Room room = new Room("My room");
        Room room2 = new Room("new Room");
        when(repository.findByLink(room.getLinkIdStudent())).thenReturn(room);
        assertNotEquals(room2,roomService.getByLink(String.valueOf(room.getLinkIdStudent())));

    }



//    @Test
//    public void roomCloseTrue()
//    {
//        Room room = new Room("My room");
//        Room room2 = new Room("My room");
//        when(repository.closeRoom(room.getLinkIdModerator())).thenReturn(true);
//        assertEquals(true,roomController.closeRoom(room.getLinkIdModerator().toString()));
//        verify(repository).closeRoom(room.getLinkIdModerator());
//    }
//
//    @Test
//    public void roomCloseFalse()
//    {
//        Room room = new Room("My room");
//        Room room2 = new Room("My room");
//        when(repository.closeRoom(room2.getLinkIdModerator())).thenReturn(true);
//        assertEquals(false,roomController.closeRoom(room.getLinkIdModerator().toString()));
//        verify(repository).closeRoom(room.getLinkIdModerator());
//    }
//
//    @Test
//    public void kickStudentsTrue()
//    {
//        Room room = new Room("My room");
//        Room room2 = new Room("My room");
//        when(repository.kickAllStudents(room.getLinkIdModerator())).thenReturn(true);
//        assertEquals(true,roomController.kickAllStudent(room.getLinkIdModerator().toString()));
//        verify(repository).kickAllStudents(room.getLinkIdModerator());
//    }
//
//    @Test
//    public void kickStudentsFalse()
//    {
//        Room room = new Room("My room");
//        Room room2 = new Room("My room");
//        when(repository.kickAllStudents(room2.getLinkIdModerator())).thenReturn(true);
//        assertEquals(false,roomController.kickAllStudent(room.getLinkIdModerator().toString()));
//        verify(repository).kickAllStudents(room.getLinkIdModerator());
//    }
//    @Test
//    public void numberOfRooms(){
//        Room room = new Room("My room");
//        Room room2 = new Room("My room");
//        roomRepository.save(room);
//        roomRepository.save(room2);
//        assertEquals(2,roomRepository.count());
//    }
}
