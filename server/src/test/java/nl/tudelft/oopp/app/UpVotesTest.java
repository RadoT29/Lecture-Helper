package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.repositories.*;
import nl.tudelft.oopp.app.services.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UpVotesTest {

    @Autowired
    private UpvoteRepository upvoteRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ModeratorRepository moderatorRepository;

    @Autowired
    private StudentRepository studentRepository;

    private Room room1;
    private Room room2;

    private Question question1;
    private Question question2;

    private Upvote upvote1;
    private Upvote upvote2;
    private Upvote upvote3;

    private Student student1;
    private Moderator mod;
    private Moderator mod2;

    /**
     * Set up.
     */
    @BeforeEach
    public void beforeEach() {
        //A question has a room associated to it.
        room1 = new Room("room1");
        roomRepository.save(room1);

        mod = new Moderator("mod", room1);
        moderatorRepository.save(mod);
        mod.setIsModerator(true);

        student1 = new Student("student", room1);
        studentRepository.save(student1);
        student1.setIsModerator(false);

        mod2 = new Moderator("mod1", room1);
        moderatorRepository.save(mod2);
        mod2.setIsModerator(true);

        question1 = new Question("question1");
        question1.setRoom(room1);
        question1.setUser(mod);
        questionRepository.save(question1);

        question2 = new Question("question2");
        question2.setRoom(room1);
        question2.setUser(mod);
        questionRepository.save(question2);

        upvote1 = new Upvote(question1, mod);
        upvote1.setValue(10);
        upvoteRepository.save(upvote1);

        upvote2 = new Upvote(question2, student1);
        upvoteRepository.save(upvote2);

        upvote3 = new Upvote(question1, mod2);
        upvote3.setValue(10);
        upvoteRepository.save(upvote3);

    }


    /**
     * Testing if the upVotes are in the database.
     */
    @Test
    public void upvoteInRepositoryTest() {
        upvote2 = upvoteRepository.getOne(upvote1.getId());
        assertEquals(upvote1, upvote2);
    }

    /**
     * Tests if the UpVote is added to the database.
     */
    @Test
    public void addUpvoteTest() {
        assertTrue(upvoteRepository.existsById(upvote1.getId()));
    }


    /**
     * Tests if the UpVote is deleted to the database.
     */
    @Test
    public void deleteUpvoteTest() {
        upvoteRepository.deleteById(upvote1.getId());

        assertFalse(upvoteRepository.existsById(upvote1.getId()));
    }

    /**
     * Tests query findUpvoteByUserAndQuestion.
     */
    @Test
    public void findUpvoteByUserAndQuestion() {
        Upvote temp = upvoteRepository.findUpvoteByUserAndQuestion(student1.getId(),
                question2.getId());

        assertEquals(temp, upvote2);
    }

    /**
     * Test query deleteUpVoteByQuestionId.
     */
    @Test
    public void deleteUpVoteByQuestionIdTest() {
        upvoteRepository.deleteUpVotesByQuestionId(question2.getId());

        assertFalse(upvoteRepository.existsById(upvote2.getId()));

    }

    /**
     * Test query incrementUpVotes.
     */
    @Test
    public void getModUpVotes() {
        Integer modUpvotes = upvoteRepository.getUpVotes(question1.getId(), room1.getId());
        assertEquals(20, modUpvotes);
    }

}
