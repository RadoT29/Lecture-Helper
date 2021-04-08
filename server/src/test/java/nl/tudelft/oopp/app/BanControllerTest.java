package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.controllers.BanController;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.User;
import nl.tudelft.oopp.app.services.QuestionService;
import nl.tudelft.oopp.app.services.RoomService;
import nl.tudelft.oopp.app.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BanControllerTest {
    @InjectMocks
    private BanController banController;

    @Mock
    private RoomService roomService;

    @Mock
    private UserService userService;

    @Mock
    private QuestionService questionService;

    private User user;

    private Room room;

    private Question question;

    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        room = new Room("My room");
        user = new User();
        question = new Question();
        request = new MockHttpServletRequest();
    }

    /**
     * This method verifies that the method banUserForThatRoom is used.
     */
    @Test
    public void testBanUserForThatRoom() {

        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(questionService.findByQuestionId(question.getId())).thenReturn(question);
        banController.banUserForThatRoom(
                String.valueOf(question.getId()),
                String.valueOf(room.getLinkIdStudent()));
        verify(userService)
                .banUserForThatRoom(String.valueOf(user.getId()), String.valueOf(room.getId()));

    }

    /**
     * This method verifies that the method warnUserForThatRoom is used.
     */
    @Test
    public void testWarnUserForThatRoom() {

        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(questionService.findByQuestionId(question.getId())).thenReturn(question);
        banController.warnUserForThatRoom(
                String.valueOf(question.getId()),
                String.valueOf(room.getLinkIdStudent()));
        verify(userService)
                .warnUserForThatRoom(String.valueOf(user.getId()), String.valueOf(room.getId()));

    }

    /**
     * This test checks the work of isUserBanned method.
     * Expected result true
     */
    @Test
    public void testIsUserBanned() {

        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(userService.isUserBanned(request.getRemoteAddr(), room.getId()))
                .thenReturn(Stream.of(-1).collect(Collectors.toList()));
        Assertions.assertTrue(
                banController.isUserBanned(String.valueOf(room.getLinkIdStudent()),request));

    }

    /**
     * This test checks the work of isUserBanned method.
     * Expected result false
     */
    @Test
    public void testIsUserBannedFalse() {

        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(userService.isUserBanned(request.getRemoteAddr(), room.getId()))
                .thenReturn(Stream.of(0).collect(Collectors.toList()));
        Assertions.assertFalse(
                banController.isUserBanned(String.valueOf(room.getLinkIdStudent()), request));

    }

    /**
     * This test checks the work of isUserWarned method.
     * Expected result true
     */
    @Test
    public void testIsUserWarned() {

        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(userService.isUserWarned(request.getRemoteAddr(), room.getId()))
                .thenReturn(Stream.of(0).collect(Collectors.toList()));
        Assertions.assertTrue(
                banController.isUserWarned(String.valueOf(room.getLinkIdStudent()), request));

    }

    /**
     * This test checks the work of isUserWarned method.
     * Expected result false
     */
    @Test
    public void testIsUserWarnedFalse() {

        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(userService.isUserWarned(request.getRemoteAddr(), room.getId()))
                .thenReturn(Stream.of(1).collect(Collectors.toList()));
        Assertions.assertFalse(
                banController.isUserWarned(String.valueOf(room.getLinkIdStudent()), request));
    }

    /**
     * This test check if the user Ip is saved on the repository.
     */
    @Test
    public void testSaveStudentIp() {

        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(userService.getByID(String.valueOf(user.getId()))).thenReturn(user);
        banController.saveStudentIp(
                String.valueOf(user.getId()),
                String.valueOf(room.getLinkIdStudent()), request);
        verify(userService).saveStudentIp(request.getRemoteAddr(), user, room);

    }

}
