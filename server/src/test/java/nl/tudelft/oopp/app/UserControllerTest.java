package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.controllers.UserController;
import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.repositories.ModeratorRepository;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import nl.tudelft.oopp.app.repositories.StudentRepository;
import nl.tudelft.oopp.app.services.QuestionService;
import nl.tudelft.oopp.app.services.RoomService;
import nl.tudelft.oopp.app.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private RoomService roomService;

    @Mock
    private UserService userService;

    @Mock
    private QuestionService questionService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private ModeratorRepository moderatorRepository;

    @Mock
    private StudentRepository studentRepository;

    private Room room;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        room = new Room("myRoom");
    }

    /**
     * Tests if a nullPointerException is thrown.
     */
    @Test
    public void roomExistsTestNull() {
        when(roomRepository.findByLink(any()))
                .thenReturn(room);

        assertThrows(NullPointerException.class,
                (Executable) userController.roomExists(null));
    }

    /**
     * Tests if when provided moderator link,
     * the proper repository saves a moderator.
     */
    @Test
    public void roomExistsTestModerator() {
        when(roomRepository.findByLink(any()))
                .thenReturn(room);

        userController.roomExists(room.getLinkIdModerator().toString());
        verify(moderatorRepository, times(1))
                .save(any());
    }

    /**
     * Tests if when provided student link,
     * the proper repository saves a student.
     */
    @Test
    public void roomExistsTestStudent() {
        when(roomRepository.findByLink(any()))
                .thenReturn(room);

        userController.roomExists(room.getLinkIdStudent().toString());
        verify(studentRepository, times(1))
                .save(any());
    }

    /**
     * Tests if the controller method executes the corresponding service method.
     */
    @Test
    public void setNickTest() {
        userController.setNickName("nickName", "1");
        verify(userService, times(1)).update(1, "nickName");
    }

    /**
     * Tests method when room's variables have not been altered.
     */
    @Test
    public void canAskQuestionTestTrue() {
        when(roomService.getByLink(any()))
                .thenReturn(room);
        assertTrue(userController.canAskQuestion("1", room.getLinkIdModerator().toString()));
    }

    /**
     * Tests method when room's variables have been altered.
     */
    @Test
    public void canAskQuestionTestFalse() {
        room.setNumberQuestionsInterval(1);
        room.setTimeInterval(1);
        when(roomService.getByLink(any()))
                .thenReturn(room);
        when(questionService
                .questionsByUserIdRoomIdInterval(anyString(), anyLong(),any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(new Question("something")));
        assertFalse(userController.canAskQuestion("1", room.getLinkIdModerator().toString()));
    }
}
