package nl.tudelft.oopp.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

import java.util.ArrayList;
import java.util.List;

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
     * Tests the query that finds all questions
     * by a room link for a moderator.
     */
    @Test
    public void testFindAllByRoomLinkModerator() {
        Room room = new Room("OurRoom");
        roomRepository.save(room);

        Question q1 = new Question("Does this work?");
        q1.setRoom(room);

        Question q2 = new Question("Different question");

        questionRepository.save(q1);
        questionRepository.save(q2);

        List<Question> res = new ArrayList<>();
        res.add(q1);

        List<Question> test = questionRepository
                .findAllByRoomLink(room.getLinkIdModerator());

        assertEquals(res, test);
    }

    /**
     * Tests the query that finds all questions
     * by a room link for a student.
     */
    @Test
    public void testFindAllByRoomLinkStudent() {
        Room room = new Room("OurRoom");
        roomRepository.save(room);

        Question q1 = new Question("Does this work?");
        q1.setRoom(room);

        Question q2 = new Question("Different question");

        questionRepository.save(q1);
        questionRepository.save(q2);

        List<Question> res = new ArrayList<>();
        res.add(q1);

        List<Question> test = questionRepository
                .findAllByRoomLink(room.getLinkIdStudent());

        assertEquals(res, test);
    }

}
