package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.models.Feedback;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.FeedbackRepository;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class FeedbackTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private RoomRepository roomRepository;

    Feedback feedback;
    Room room;

    /**
     * Add test entries to the database.
     */
    @BeforeEach
    public void setup() {
        room = new Room("room name");
        roomRepository.save(room);

        feedback = new Feedback();
        feedback.setRoom(room);
        feedback.setComment("nice");
        feedback.setRating(4);

        feedbackRepository.save(feedback);
    }

    /**
     * To delete test entries from the database.
     */
    @AfterEach
    public void end() {
        roomRepository.delete(room);
        feedbackRepository.delete(feedback);
    }

    /**
     * test if the feedback is correctly saved in the database.
     */
    @Test
    public void saveFeedbackTest() {

        Feedback actual = feedbackRepository.getOne(feedback.getId());

        //timestamps - assigned automatically
        feedback.setCreatedAt(actual.getCreatedAt());
        feedback.setUpdatedAt(actual.getUpdatedAt());

        assertEquals(feedback, actual);
    }


    /**
     * Check if the size of the retrieved feedback list.
     */
    @Test
    public void findAllByRoomIdSizeTest() {
        List<Feedback> actual = feedbackRepository.findAllByRoomId(room.getId());
        assertEquals(1, actual.size());
    }

    /**
     * Check the content of the retrieved feedback list.
     */
    @Test
    public void findAllByRoomIdContentTest() {
        List<Feedback> actual = feedbackRepository.findAllByRoomId(room.getId());
        //timestamps - assigned automatically
        feedback.setCreatedAt(actual.get(0).getCreatedAt());
        feedback.setUpdatedAt(actual.get(0).getUpdatedAt());
        List<Feedback> expected = List.of(feedback);
        assertEquals(expected, actual);
    }

}
