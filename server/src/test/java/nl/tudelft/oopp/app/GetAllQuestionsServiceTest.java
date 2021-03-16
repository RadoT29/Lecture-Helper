package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.repositories.QuestionRepository;
import nl.tudelft.oopp.app.services.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class GetAllQuestionsServiceTest {

    @MockBean
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService service;

    private Room room1;
    private Room room2;
    private Question question1;
    private Question question2;
    private Question question3;


    /**
     * initializes the objects for the mock repository.
     * room1: id = 12, name = "Room_1", Student/Moderator links initialized
     * room2: name = "Room_2"
     * question1: id=1, text="Question1", room=room1, user=moderator(id=1, name="Adam1", room=room1)
     * question2: id=2, text="Question2", room=room2, user=student(id=2, name="Adam2", room=room2)
     * question3: id=3, text="Question3", room=room1, user=student(id=3, name="Adam3", room=room1)
     */
    @BeforeEach
    public void beforeEach() {
        room1 = new Room();
        room1.setName("Room_1");
        room1.setLinkIdModerator();
        room1.newLinkIdStudent();
        room1.setId(12);

        room2 = new Room();
        room2.setName("Room_2");

        User user1 = new Moderator("Adam1", room1);
        user1.setId(1);
        User user2 = new Student("Adam2", room2);
        user2.setId(2);
        User user3 = new Moderator("Adam3", room1);
        user3.setId(3);

        question1 = new Question("Question1");
        question1.setId(1);
        question1.setRoom(room1);
        question1.setUser(user1);

        question2 = new Question("Question2");
        question2.setId(2);
        question2.setRoom(room2);
        question2.setUser(user2);

        question3 = new Question("Question3");
        question3.setId(3);
        question3.setRoom(room1);
        question3.setUser(user2);
    }

    /**
     * tests if the method correctly returns all questions stored in the database.
     */
    @Test
    public void getAllQuestions_Test() {

        List<Question> allQuestions = new ArrayList<>();
        allQuestions.add(question1);
        allQuestions.add(question2);
        allQuestions.add(question3);

        Mockito.when(questionRepository.findAll())
                .thenReturn(allQuestions);

        List<Question> result = service.getAllQuestions();

        assertEquals(allQuestions, result);
    }

    /**
     * tests if the function correctly returns
     *      all questions for a specific room (identified by the LinkIdModerator)
     *      that are stored in the database.
     */
    @Test
    public void getAllQuestionsByRoom_ModeratorLink_Test() {

        List<Question> resultQuestions = new ArrayList<>();
        resultQuestions.add(question3);
        resultQuestions.add(question1);

        //we don't have to change userid and roomid to 0's to check if the results are the same
        //this is because they will be changed in the service method we are testing
        //zero ids are tested in different tests

        UUID linkToTest = room1.getLinkIdModerator();

        Mockito.when(questionRepository.findAllByRoomLink(linkToTest))
                .thenReturn(resultQuestions);

        List<Question> result = service.getAllQuestionsByRoom(linkToTest.toString());

        assertEquals(resultQuestions, result);
    }

    /**
     * tests if the function correctly returns
     *      all questions for a specific room (identified by the LinkIdStudent)
     *      that are stored in the database.
     */
    @Test
    public void getAllQuestionsByRoom_StudentLink_Test() {

        List<Question> resultQuestions = new ArrayList<>();
        resultQuestions.add(question3);
        resultQuestions.add(question1);

        //we don't have to change userid and roomid to 0's to check if the results are the same
        //this is because they will be changed in the service method we are testing
        //zero ids are tested in different tests

        UUID linkToTest = room1.getLinkIdStudent();

        Mockito.when(questionRepository.findAllByRoomLink(linkToTest))
                .thenReturn(resultQuestions);

        List<Question> result = service.getAllQuestionsByRoom(linkToTest.toString());

        assertEquals(resultQuestions, result);
    }

    /**
     * tests it the questions returned by the method have the userid and roomid changed to 0's.
     */
    @Test
    public void getAllQuestionsByRoom_roomIdZero_Test() {

        List<Question> resultQuestions = new ArrayList<>();
        resultQuestions.add(question3);
        resultQuestions.add(question1);

        //it does not matter which link we will use, because we test both of them in different tests
        UUID linkToTest = room1.getLinkIdModerator();

        Mockito.when(questionRepository.findAllByRoomLink(linkToTest))
                .thenReturn(resultQuestions);

        service.getAllQuestionsByRoom(linkToTest.toString());
        assertTrue(question1.getRoom().getId() == 0
                && question3.getRoom().getId() == 0);
    }

    /**
     * tests it the questions returned by the method have the userid and userid changed to 0's.
     */
    @Test
    public void getAllQuestionsByRoom_userIdZero_Test() {

        List<Question> resultQuestions = new ArrayList<>();
        resultQuestions.add(question3);
        resultQuestions.add(question1);

        //it does not matter which link we will use, because we test both of them in different tests
        UUID linkToTest = room1.getLinkIdModerator();

        Mockito.when(questionRepository.findAllByRoomLink(linkToTest))
                .thenReturn(resultQuestions);

        service.getAllQuestionsByRoom(linkToTest.toString());
        assertTrue(question1.getUser().getId() == 0
                && question3.getUser().getId() == 0);
    }

}
