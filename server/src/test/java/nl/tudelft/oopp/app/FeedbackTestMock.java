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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class FeedbackTestMock {

    @InjectMocks
    private FeedbackService feedbackService;

    @Mock
    private FeedbackRepository feedbackRepository;
    @Mock
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * test if the feedback list is retrieved correctly.
     */
    @Test
    public void getFeedbackContentTest() {
        Room room = new Room("room name");
        String roomLink = room.getLinkIdModerator() + "";
        when(roomService.getByLink(roomLink)).thenReturn(room);

        Feedback feedback = new Feedback();
        feedback.setRoom(room);
        feedback.setComment("nice");
        feedback.setRating(4);

        when(feedbackRepository.findAllByRoomId(room.getId()))
                .thenReturn(Stream.of(feedback).collect(Collectors.toList()));

        assertEquals(List.of(feedback), feedbackService.getFeedback(roomLink));
    }
}
