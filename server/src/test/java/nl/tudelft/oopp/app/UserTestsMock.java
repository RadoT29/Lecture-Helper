package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.controllers.UserController;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.IpAddressRepository;
import nl.tudelft.oopp.app.repositories.QuestionRepository;
import nl.tudelft.oopp.app.repositories.RoomRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class UserTestsMock {

    @InjectMocks
    private UserService userService;

    @Mock
    private IpAddressRepository ipAddressRepository;

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private RoomService roomService;

    @Mock
    private QuestionService questionService2;

    @InjectMocks
    private UserController userController;

    @Autowired
    private RoomRepository roomRepository;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    /**
     * Method check if the user is banned for that specific room.
     * In this case it is not.
     */
    @Test
    public void userIsNotBanned() {
        when(ipAddressRepository.checkForIpBan("2020", 3))
                .thenReturn(Stream.of(1, 1)
                        .collect(Collectors.toList()));
        Assertions.assertFalse(userService.isUserBanned("2020", 3L).contains(-1));
    }

    /**
     * Method check if the user is banned for that specific room.
     * In this case it is.
     */
    @Test
    public void userIsBanned() {
        when(ipAddressRepository.checkForIpBan("2020", 3))
                .thenReturn(Stream.of(1, 1)
                        .collect(Collectors.toList()));
        Assertions.assertTrue(userService.isUserBanned("2020", 3L).contains(-1));
    }

    /**
     * This method tests if when question repository is called, it returns a list of
     * fixed number of questions. In this case true
     */
    @Test
    public void canUserAskQuestionReturnedNumberQuestionsTrue() {
        LocalDateTime localDateTime = LocalDateTime.now();
        when(questionRepository.questionsByUserIdRoomIdInterval(2L, 2L, localDateTime))
                .thenReturn(Stream.of(new Question(), new Question(), new Question())
                        .collect(Collectors.toList()));
        assertEquals(3,questionService
                .questionsByUserIdRoomIdInterval(String.valueOf(2L), 2L, localDateTime).size());
    }

    /**
     * This method tests if when question repository is called, it returns a list of
     * fixed number of questions. In this case false
     */
    @Test
    public void canUserAskQuestionReturnedNumberQuestionsFalse() {
        LocalDateTime localDateTime = LocalDateTime.now();
        when(questionRepository.questionsByUserIdRoomIdInterval(2L, 2L, localDateTime))
                .thenReturn(Stream.of(new Question(), new Question(), new Question())
                        .collect(Collectors.toList()));
        assertNotEquals(2,questionService
                .questionsByUserIdRoomIdInterval(String.valueOf(2L), 2L, localDateTime).size());
    }

    /**
     * This method tests if the user is in limit of allowed questions to ask.
     */
    @Test
    public void canUserAskQuestion() {
        Room room = new Room("my room");
        LocalDateTime localDateTime = LocalDateTime.now();
        when(roomService.getByLink(room.getLinkIdStudent().toString())).thenReturn(room);
        when(questionRepository.questionsByUserIdRoomIdInterval(2L, room.getId(), localDateTime))
                .thenReturn(Stream.of(new Question(), new Question(), new Question())
                        .collect(Collectors.toList()));
        assertTrue(userController.canAskQuestion(
                String.valueOf(1),room.getLinkIdStudent().toString()));
    }

}
