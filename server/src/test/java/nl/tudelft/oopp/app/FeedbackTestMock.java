package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.models.Feedback;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.FeedbackRepository;
import nl.tudelft.oopp.app.services.FeedbackService;
import nl.tudelft.oopp.app.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class FeedbackTestMock {

    @InjectMocks
    private FeedbackService feedbackService;

    @Mock
    private FeedbackRepository feedbackRepository;
    @Mock
    private RoomService roomService;

    private Room room;
    private Feedback feedback;
    private Feedback tooLongFeedback;
    private String roomLink;
    private String falseLink;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        room = new Room("room name");
        roomLink = room.getLinkIdModerator() + "";
        when(roomService.getByLink(roomLink)).thenReturn(room);

        feedback = new Feedback();
        feedback.setComment("nice");
        feedback.setRating(4);

        tooLongFeedback = new Feedback();
        tooLongFeedback.setComment("Lorem ipsum dolor sit amet, "
                + "consectetur adipiscing elit. Ut ac mi nibh. "
                + "Mauris pulvinar convallis bibendum. Quisque finibus, "
                + "mi eget pretium feugiat, dolor libero ornare nisl, "
                + "eu laoreet nisi enim eu felis. "
                + "Aliquam placerat, ex ac molestie congue, mi nec.");
        tooLongFeedback.setRating(2);

        falseLink = "FalseLink";
        when(roomService.getByLink(falseLink)).thenReturn(null);
    }

    //TEST addFeedback(String roomLink, Feedback feedback)

    /**
     * Test if the feedbackRepository saves the feedback when is of correct length.
     */
    @Test
    public void addFeedbackOkCommentLengthTest() {
        feedbackService.addFeedback(roomLink, feedback);
        verify(feedbackRepository, times(1)).save(feedback);
    }

    /**
     * Tests that if the feedback comment is too long (>254 chars)
     * then the feedback is not saved and its room is not set.
     */
    @Test
    public void addFeedbackCommentTooLongTest() {
        feedbackService.addFeedback(roomLink, tooLongFeedback);
        assertNull(feedback.getRoom());
        verify(feedbackRepository, times(0)).save(tooLongFeedback);
    }

    /**
     * Tests that if the room does not exist AssertionError is thrown
     * and the feedback is not saved.
     */
    @Test
    public void addFeedbackNullTest() {
        assertThrows(AssertionError.class, () -> feedbackService.addFeedback(roomLink, null));
        verify(feedbackRepository, times(0)).save(feedback);
    }

    /**
     * Tests that if the room does not exist then the AssertionError is thrown
     * and the feedback is not saved.
     */
    @Test
    public void addFeedbackRoomDoesNotExistTest() {
        assertThrows(AssertionError.class, () -> feedbackService.addFeedback(falseLink, feedback));
        verify(feedbackRepository, times(0)).save(feedback);
    }

    /**
     * tests if the room of the feedback is set to a specified by the roomLink room.
     */
    @Test
    public void isFeedbackRoomSet() {
        assertNull(feedback.getRoom());
        feedbackService.addFeedback(roomLink, feedback);
        assertEquals(room, feedback.getRoom());
    }



    //TEST getFeedback(String roomLink)
    /**
     * test if the feedback list is retrieved correctly.
     */
    @Test
    public void getFeedbackContentTest() {
        when(feedbackRepository.findAllByRoomId(room.getId()))
                .thenReturn(Stream.of(feedback).collect(Collectors.toList()));
        assertEquals(List.of(feedback), feedbackService.getFeedback(roomLink));
    }

    /**
     * tests that AssertionError is thrown when the room does not exist.
     */
    @Test
    public void getFeedbackRoomDoesNotExistTest() {
        assertThrows(AssertionError.class, () -> feedbackService.getFeedback(falseLink));
        verify(feedbackRepository, times(0)).findAllByRoomId(room.getId());
    }

}
