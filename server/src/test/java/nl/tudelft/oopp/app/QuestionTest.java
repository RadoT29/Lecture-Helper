package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Student;
import nl.tudelft.oopp.app.models.Moderator;
import nl.tudelft.oopp.app.repositories.QuestionRepository;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import nl.tudelft.oopp.app.repositories.StudentRepository;
import nl.tudelft.oopp.app.repositories.ModeratorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class QuestionTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModeratorRepository moderatorRepository;

    /**
     * This test saves a Question on the database and then retrieves it
     * to assert that it's the same.
     */
    @Test
    public void saveAndRetrieveQuestion() {

        //A question has a room associated to it
        Room room = new Room("room name");
        roomRepository.save(room);

        //A question has a user associated to it
        Student student = new Student("Radoslav",room);
        studentRepository.save(student);

        Question question = new Question("Does this test work?");
        question.setRoom(room);
        question.setUser(student);
        questionRepository.save(question);


        Question qestion2 = questionRepository.getOne(question.getId());
        assertEquals(question, qestion2);
    }

    /**
     * This test saves a Question on the database and then asserts
     * that the associated Room is the same one.
     */
    @Test
    public void saveAndRetrieveRoomViaQuestion() {
        Room room = new Room("OurRoom");
        roomRepository.save(room);

        Moderator mod = new Moderator("Pedro",room);
        moderatorRepository.save(mod);

        Question question = new Question("Does this test work?");
        question.setRoom(room);
        question.setUser(mod);
        questionRepository.save(question);

        Question question2 = questionRepository.getOne(question.getId());
        assertEquals(question2.getRoom(), room);
        assertEquals(question2.getRoom().getName(), "OurRoom");

    }

    /**
     * This test saves a Question on the database and then asserts
     * that the associated User is the same one.
     */
    @Test
    public void saveAndRetrieveUserViaQuestion() {
        Room room = new Room("OurRoom");
        roomRepository.save(room);

        Moderator mod = new Moderator("Natalia",room);
        moderatorRepository.save(mod);

        Question question = new Question("Does this test work?");
        question.setRoom(room);
        question.setUser(mod);
        questionRepository.save(question);

        Question question2 = questionRepository.getOne(question.getId());
        assertEquals(question2.getUser(), mod);
        assertEquals(question2.getUser().getName(), "Natalia");

    }

    /**
     * Count the number of questions on the repository
     */
    @Test
    public void findTheSizeOffAllQuestions(){
        Question question1 = new Question("question1");
        Question question2 = new Question("question2");
        Question question3 = new Question("question3");
        questionRepository.save(question1);
        questionRepository.save(question2);
        questionRepository.save(question3);
        assertEquals(3,questionRepository.count());
    }

//    /**
//     * Tests if the question is deleted from the database.
//     */
//    @Test
//    public void dismissTest() {
//        Question question = new Question("This will be deleted");
//        long deletedId = question.getId();
//        questionRepository.save(question);
////        assertTrue(questionRepository.existsById(deletedId));
////        questionRepository.deleteById(deletedId);
//        assertFalse(questionRepository.existsById(deletedId));
//    }
    /**
     * Tests if the question is deleted from the database.
     */
    @Test
    public void dismissTest() {
        Question question = new Question("This will be deleted");
        questionRepository.save(question);
        assertTrue(questionRepository.existsById(question.getId()));
        questionRepository.deleteById(question.getId());
        assertFalse(questionRepository.existsById(question.getId()));
    }

    @Test
    public void findAllByRoomLinkTest() {
        Room room1 = new Room("Room1");
        room1.setLinkIdModerator();
        Room room2 = new Room("Room2");
        roomRepository.save(room1);
        roomRepository.save(room2);

        Question question1 = new Question("should be in the result");
        question1.setRoom(room1);

        Question question2 = new Question("should not be in the result");
        question2.setRoom(room2);

        questionRepository.save(question1);
        questionRepository.save(question2);

        List<Question> expected = List.of(question1);
        List<Question> result = questionRepository.findAllByRoomLink(room1.getLinkIdModerator());

        assertEquals(expected, result);
    }

}
