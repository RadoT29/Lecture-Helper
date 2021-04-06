package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.controllers.FeedbackController;
import nl.tudelft.oopp.app.models.Feedback;
import nl.tudelft.oopp.app.services.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    private FeedbackController feedbackController;

    private String roomLink;
    private Feedback feedback;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        feedbackController = new FeedbackController(feedbackService);
        roomLink = "123";
        feedback = new Feedback();
    }

    /**
     * Tests if the right feedbackService method is called when adding new feedback.
     */
    @Test
    public void addFeedbackTest() {
        feedbackController.addFeedback(roomLink, feedback);
        verify(feedbackService, times(1)).addFeedback(roomLink, feedback);
    }

    /**
     * Tests getFeedback method in the FeedbackController class.
     */
    @Test
    public void getFeedbackTest() {
        when(feedbackService.getFeedback(roomLink))
                .thenReturn(Stream.of(feedback).collect(Collectors.toList()));
        assertEquals(List.of(feedback), feedbackController.getFeedback(roomLink));
    }

}
