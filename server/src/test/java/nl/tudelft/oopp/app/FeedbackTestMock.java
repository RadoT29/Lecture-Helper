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
    private String roomLinkModerator;
    private String roomLinkStudent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        room = new Room("room name");
        room.setLinkIdModerator();
        room.newLinkIdStudent();
        roomLinkModerator = room.getLinkIdModerator() + "";
        roomLinkStudent = room.getLinkIdStudent() + "";
        when(roomService.getByLinkModerator(roomLinkModerator)).thenReturn(room);
        when(roomService.getByLink(roomLinkModerator)).thenReturn(room);
        when(roomService.getByLink(roomLinkStudent)).thenReturn(room);

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

    }

    //TEST addFeedback(String roomLink, Feedback feedback)

    /**
     * Test if the feedbackRepository saves the feedback when is of correct length.
     */
    @Test
    public void addFeedbackOkCommentLengthTest() {
        feedbackService.addFeedback(roomLinkStudent, feedback);
        verify(feedbackRepository, times(1)).save(feedback);
    }

    /**
     * Tests that if the feedback comment is too long (>254 chars)
     * then the feedback is not saved and its room is not set.
     */
    @Test
    public void addFeedbackCommentTooLongTest() {
        feedbackService.addFeedback(roomLinkStudent, tooLongFeedback);
        assertNull(feedback.getRoom());
        verify(feedbackRepository, times(0)).save(tooLongFeedback);
    }



    /**
     * tests if the room of the feedback is set to a specified by the roomLink room.
     */
    @Test
    public void isFeedbackRoomSet() {
        assertNull(feedback.getRoom());
        feedbackService.addFeedback(roomLinkStudent, feedback);
        assertEquals(room, feedback.getRoom());
    }



    //TEST getFeedback(String roomLink)
    /**
     * test if the feedback list is retrieved correctly, when the moderator link is provided.
     */
    @Test
    public void getFeedbackContent_ModeratorLink_Test() {
        when(roomService.getByLinkModerator(roomLinkModerator)).thenReturn(room);
        when(feedbackRepository.findAllByRoomId(room.getId()))
                .thenReturn(Stream.of(feedback).collect(Collectors.toList()));
        assertEquals(List.of(feedback), feedbackService.getFeedback(roomLinkModerator));
    }

    /**
     * test if the feedback list is retrieved correctly, when the moderator link is provided.
     */
    @Test
    public void getFeedbackContent_StudentLink_Test() {
        assertThrows(NullPointerException.class,
                () -> feedbackService.getFeedback(roomLinkStudent));
    }


}
