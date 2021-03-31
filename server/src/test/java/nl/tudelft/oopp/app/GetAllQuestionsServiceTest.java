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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
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

}
