package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.controllers.BanController;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.User;
import nl.tudelft.oopp.app.services.QuestionService;
import nl.tudelft.oopp.app.services.RoomService;
import nl.tudelft.oopp.app.services.UserService;
import org.apache.coyote.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import java.net.http.HttpRequest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveStudentIp(){
        Room room = new Room("My room");
        User user = new User();
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(userService.getByID(String.valueOf(user.getId()))).thenReturn(user);
        banController.saveStudentIp(String.valueOf(user.getId()), String.valueOf(room.getLinkIdStudent()),request);
        verify(userService).saveStudentIp(request.getRemoteAddr(),user,room);
    }

    @Test
    public void testBanUserForThatRoom(){
        Room room = new Room("My room");
        User user = new User();
        Question question = new Question();
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(questionService.findByQuestionId(question.getId())).thenReturn(question);
        banController.banUserForThatRoom(String.valueOf(question.getId()), String.valueOf(room.getLinkIdStudent()));
        verify(userService).banUserForThatRoom(String.valueOf(user.getId()), String.valueOf(room.getId()));
    }

    @Test
    public void testWarnUserForThatRoom(){
        Room room = new Room("My room");
        User user = new User();
        Question question = new Question();
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(questionService.findByQuestionId(question.getId())).thenReturn(question);
        banController.warnUserForThatRoom(String.valueOf(question.getId()), String.valueOf(room.getLinkIdStudent()));
        verify(userService).warnUserForThatRoom(String.valueOf(user.getId()), String.valueOf(room.getId()));
    }

    @Test
    public void testIsUserBanned(){
        Room room = new Room("My room");
        User user = new User();
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(userService.isUserBanned(request.getRemoteAddr(),room.getId())).thenReturn(Stream.of(-1).collect(Collectors.toList()));
        assertTrue(banController.isUserBanned(String.valueOf(room.getLinkIdStudent()),request));
    }

    @Test
    public void testIsUserBannedFalse(){
        Room room = new Room("My room");
        User user = new User();
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(userService.isUserBanned(request.getRemoteAddr(),room.getId())).thenReturn(Stream.of(0).collect(Collectors.toList()));
        assertFalse(banController.isUserBanned(String.valueOf(room.getLinkIdStudent()),request));
    }

    @Test
    public void testIsUserWarned(){
        Room room = new Room("My room");
        User user = new User();
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(userService.isUserWarned(request.getRemoteAddr(),room.getId())).thenReturn(Stream.of(0).collect(Collectors.toList()));
        assertTrue(banController.isUserWarned(String.valueOf(room.getLinkIdStudent()),request));
    }

    @Test
    public void testIsUserWarnedFalse(){
        Room room = new Room("My room");
        User user = new User();
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(userService.isUserWarned(request.getRemoteAddr(),room.getId())).thenReturn(Stream.of(1).collect(Collectors.toList()));
        assertFalse(banController.isUserWarned(String.valueOf(room.getLinkIdStudent()),request));
    }


}
