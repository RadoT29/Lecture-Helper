package nl.tudelft.oopp.app;

import java.util.ArrayList;
import java.util.List;
import nl.tudelft.oopp.app.controllers.PollController;
import nl.tudelft.oopp.app.models.Poll;
import nl.tudelft.oopp.app.models.PollAnswer;
import nl.tudelft.oopp.app.services.PollService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class PollControllerTest {

    @InjectMocks
    PollController pollController;

    @Mock
    PollService pollService;

    private final String roomLink = "";
    private final long userId = 1;

    private long pollId;
    private Poll poll;
    private List<PollAnswer> answers;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(pollController);
        poll = new Poll();
        pollId = poll.getId();

        answers = new ArrayList<>();
        PollAnswer pollAnswer1 = new PollAnswer();
        PollAnswer pollAnswer2 = new PollAnswer();
        answers.add(pollAnswer1);
        answers.add(pollAnswer2);
    }

    @Test
    void getPollsTest() {
        pollController.getPolls(roomLink);
        verify(pollService,times(1)).getPolls(roomLink);
    }

    @Test
    void createPollTestEquals() {
        when(pollController.createPoll(roomLink)).thenReturn(pollId);
        assertEquals(pollId,pollController.createPoll(roomLink));
    }

    @Test
    void createPollTestNotEquals() {
        when(pollController.createPoll(roomLink)).thenReturn(pollId);
        assertNotEquals(2,pollController.createPoll(roomLink));
    }

    @Test
    void updatePollTest() {
        pollController.updatePoll(roomLink,pollId,poll);
        verify(pollService,times(1)).updateAndOpenPoll(roomLink,pollId,poll);
    }

    @Test
    void finishPollTest() {
        pollController.finishPoll(roomLink,pollId);
        verify(pollService,times(1)).finishPoll(roomLink,pollId);
    }

    @Test
    void getAnswerTestEquals() {

        when(pollService.getAnswers(pollId,userId)).thenReturn(answers);
        assertEquals(answers, pollController.getAnswers(pollId,userId));
    }

    @Test
    void getAnswerTestNotEquals() {
        when(pollService.getAnswers(pollId,1)).thenReturn(new ArrayList<PollAnswer>());
        assertNotEquals(answers, pollController.getAnswers(pollId,userId));
    }

    @Test
    void createAnswersTest() {
        pollController.createAnswers(userId,poll);
        verify(pollService, times(1)).createAnswers(userId,poll);
    }
}
