package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.controllers.ReactionController;
import nl.tudelft.oopp.app.models.EmotionReaction;
import nl.tudelft.oopp.app.models.SpeedReaction;
import nl.tudelft.oopp.app.services.ReactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class ReactionControllerTest {
    @Mock
    private ReactionService reactionService;

    private ReactionController reactionController;

    private String roomLink;
    private String userId;
    private SpeedReaction speedReaction;
    private EmotionReaction emotionReaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        reactionController = new ReactionController(reactionService);
        roomLink = "123";
        userId = "1";
        speedReaction = new SpeedReaction();
        emotionReaction = new EmotionReaction();
    }

    /**
     * test if the right reaction service method is called when adding a new speed reaction.
     */
    @Test
    public void addSpeedReactionTest() {
        reactionController.add(roomLink, userId, speedReaction);
        verify(reactionService, times(1)).addNewReaction(roomLink, userId, speedReaction);
    }

    /**
     * test if the right reaction service method is called when adding a new emotion reaction.
     */
    @Test
    public void addEmotionReactionTest() {
        reactionController.add(roomLink, userId, emotionReaction);
        verify(reactionService, times(1)).addNewReaction(roomLink, userId, emotionReaction);
    }

    /**
     * test if the right reaction service method is called when getting speed statistics.
     */
    @Test
    public void getSpeedStatTest() {
        reactionController.getSpeedStat(roomLink);
        verify(reactionService, times(1)).getSpeedStat(roomLink);
    }

    /**
     * test if the right reaction service method is called when getting emotion counts.
     */
    @Test
    public void getEmotionCountsTest() {
        reactionController.getEmotionCounts(roomLink);
        verify(reactionService, times(1)).getEmotionCounts(roomLink);
    }
}
