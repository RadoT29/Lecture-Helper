package nl.tudelft.oopp.app;


import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.repositories.EmotionReactionRepository;
import nl.tudelft.oopp.app.repositories.SpeedReactionRepository;
import nl.tudelft.oopp.app.services.ReactionService;
import nl.tudelft.oopp.app.services.RoomService;
import nl.tudelft.oopp.app.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class ReactionServiceTest {

    @InjectMocks
    private ReactionService reactionService;

    @Mock
    private SpeedReactionRepository speedReactionRepository;
    @Mock
    private EmotionReactionRepository emotionReactionRepository;
    @Mock
    private RoomService roomService;
    @Mock
    private UserService userService;

    private Room room;
    private Long roomId;
    private String roomLink;

    private User user;
    private String userIdStr;
    private Long userId;

    private SpeedReaction speedReaction;
    private Long speedId;

    private EmotionReaction emotionReaction;
    private Long emotionId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        room = new Room("room name");
        roomId = room.getId();
        roomLink = room.getLinkIdModerator() + "";
        when(roomService.getByLink(roomLink)).thenReturn(room);

        user = new Student();
        userIdStr = user.getId() + "";
        userId = user.getId();
        when(userService.getByID(userIdStr)).thenReturn(user);

        speedReaction = new SpeedReaction();
        speedReaction.setId(1);
        speedId = speedReaction.getId();
        speedReaction.setValue(1);

        emotionReaction = new EmotionReaction();
        speedReaction.setId(2);
        emotionId = emotionReaction.getId();
        emotionReaction.setValue(0);
    }

    //TEST addNewReaction(String roomLink, String userId, Reaction reaction)

    /**
     * test if the user is correctly set to the reaction before adding.
     */
    @Test
    public void addNewReaction_setUser_test() {
        when(speedReactionRepository.getReactionIdByRoomAndUserIds(roomId, userId))
                .thenReturn(null);
        reactionService.addNewReaction(roomLink, userIdStr, speedReaction);
        assertEquals(user, speedReaction.getUser());
    }


    /**
     * test if the room is correctly set to the reaction before adding.
     */
    @Test
    public void addNewReaction_setRoom_test() {
        when(speedReactionRepository.getReactionIdByRoomAndUserIds(roomId, userId))
                .thenReturn(null);
        reactionService.addNewReaction(roomLink, userIdStr, speedReaction);
        assertEquals(room, speedReaction.getRoom());
    }

    //test interactions when the type is known

    /**
     * test that if the reaction is of type SpeedReaction there is no interaction
     * with the EmotionReactionRepository.
     */
    @Test
    public void addNewReaction_speed_noEmotionInteraction_test() {
        reactionService.addNewReaction(roomLink, userIdStr, speedReaction);
        verifyNoInteractions(emotionReactionRepository);
    }

    /**
     * test that if the reaction is of type EmotionReaction there is no interaction
     * with the SpeedReactionRepository.
     */
    @Test
    public void addNewReaction_emotion_noSpeedInteraction_test() {
        reactionService.addNewReaction(roomLink, userIdStr, emotionReaction);
        verifyNoInteractions(speedReactionRepository);
    }


    //test getting reactionId
    /**
     * test if the right method is called when looking for the speedReactionId.
     */
    @Test
    public void addNewReaction_speed_getReactionId_test() {
        reactionService.addNewReaction(roomLink, userIdStr, speedReaction);
        verify(speedReactionRepository, times(1))
                .getReactionIdByRoomAndUserIds(roomId, userId);
    }

    /**
     * test if the right method is called when looking for the speedReactionId.
     */
    @Test
    public void addNewReaction_emotion_getReactionId_test() {
        reactionService.addNewReaction(roomLink, userIdStr, emotionReaction);
        verify(emotionReactionRepository, times(1))
                .getReactionIdByRoomAndUserIds(roomId, userId);
    }

    //test update

    /**
     * test that when a speed reaction was found in the db then update method is called.
     */
    @Test
    public void addNewReaction_speed_update_test() {
        when(speedReactionRepository.getReactionIdByRoomAndUserIds(roomId, userId))
                .thenReturn(speedId);
        reactionService.addNewReaction(roomLink, userIdStr, speedReaction);
        verify(speedReactionRepository, times(1))
                .updateValue(speedId, speedReaction.getValue());
    }

    /**
     * test that when an emotion reaction was found in the db then update method is called.
     */
    @Test
    public void addNewReaction_emotion_update_test() {
        when(emotionReactionRepository.getReactionIdByRoomAndUserIds(roomId, userId))
                .thenReturn(emotionId);
        reactionService.addNewReaction(roomLink, userIdStr, emotionReaction);
        verify(emotionReactionRepository, times(1))
                .updateValue(emotionId, emotionReaction.getValue());
    }

    //test add new

    /**
     * test that when a speed reaction was NOT found in the db then save method is called.
     */
    @Test
    public void addNewReaction_speed_save_test() {
        when(speedReactionRepository.getReactionIdByRoomAndUserIds(roomId, userId))
                .thenReturn(null);
        reactionService.addNewReaction(roomLink, userIdStr, speedReaction);
        verify(speedReactionRepository, times(1)).save(speedReaction);
    }

    /**
     * test that when a speed reaction was NOT found in the db then save method is called.
     */
    @Test
    public void addNewReaction_emotion_save_test() {
        when(emotionReactionRepository.getReactionIdByRoomAndUserIds(roomId, userId))
                .thenReturn(null);

        reactionService.addNewReaction(roomLink, userIdStr, emotionReaction);
        verify(emotionReactionRepository, times(1)).save(emotionReaction);
    }


    //TEST getSpeedStat(String roomLink)

    /**
     * tests if the method correctly evaluated the speed statistics.
     * and did not throw an exception.
     */
    @Test
    public void getSpeedStat_zeroPositive_test() {
        // 5*(1) + 1*(-1) + 4*(0) = 4
        //4/10 ~ 0
        when(speedReactionRepository.getSpeedSum(roomId)).thenReturn(4);
        when(speedReactionRepository.getSpeedCount(roomId)).thenReturn(10);
        assertEquals(0, reactionService.getSpeedStat((roomLink)));
    }

    /**
     * tests if the method correctly evaluated the speed statistics.
     */
    @Test
    public void getSpeedStat_zeroNegative_test() {
        // 5*(-1) + 1*(1) + 4*(0) = -4
        //-4/10 ~ 0
        when(speedReactionRepository.getSpeedSum(roomId)).thenReturn(-4);
        when(speedReactionRepository.getSpeedCount(roomId)).thenReturn(10);
        assertEquals(0, reactionService.getSpeedStat((roomLink)));
    }

    /**
     * tests if the method correctly evaluated the speed statistics.
     */
    @Test
    public void getSpeedStat_zero_test() {
        // 6*(1) + 1*(-1) + 3*(0) = 5
        // 5/10 ~ 1
        when(speedReactionRepository.getSpeedSum(roomId)).thenReturn(5);
        when(speedReactionRepository.getSpeedCount(roomId)).thenReturn(10);
        assertEquals(1, reactionService.getSpeedStat((roomLink)));
    }

    /**
     * tests if the method correctly evaluated the speed statistics.
     */
    @Test
    public void getSpeedStat_one_test() {
        // 7*(1) + 1*(-1) + 2*(0) = 6
        // 6/10 ~ 1
        when(speedReactionRepository.getSpeedSum(roomId)).thenReturn(6);
        when(speedReactionRepository.getSpeedCount(roomId)).thenReturn(10);
        assertEquals(1, reactionService.getSpeedStat((roomLink)));
    }

    /**
     * tests if the method correctly evaluated the speed statistics.
     */
    @Test
    public void getSpeedStat_minusOne_test() {
        // 7*(-1) + 1*(1) + 2*(0) = -6
        // -6/10 ~ -1
        when(speedReactionRepository.getSpeedSum(roomId)).thenReturn(-6);
        when(speedReactionRepository.getSpeedCount(roomId)).thenReturn(10);
        assertEquals(-1, reactionService.getSpeedStat((roomLink)));
    }

    /**
     * tests if one of the repository calls throws an exception 0 will be returned.
     */
    @Test
    public void getSpeedStat_repositoryException_test() {
        // 7*(-1) + 1*(1) + 2*(0) = -6
        // -6/10 ~ -1
        when(speedReactionRepository.getSpeedSum(roomId)).thenReturn(-6);
        when(speedReactionRepository.getSpeedCount(roomId)).thenThrow(new NullPointerException());
        assertEquals(0, reactionService.getSpeedStat((roomLink)));
    }

    /**
     * tests if the count of reactions is 0 (can't divide by 0)
     * the exception is caught and 0 is returned.
     */
    @Test
    public void getSpeedStat_noReactionsException_test() {
        // 7*(-1) + 1*(1) + 2*(0) = -6
        // -6/10 ~ -1
        when(speedReactionRepository.getSpeedSum(roomId)).thenReturn(-6);
        when(speedReactionRepository.getSpeedCount(roomId)).thenReturn(0);
        assertEquals(0, reactionService.getSpeedStat((roomLink)));
    }

    //TEST getEmotionCounts(String roomLink)

    /**
     * tests if the counts of the emotion reactions are returned in the correctly.
     */
    @Test
    public void getEmotionCounts_test() {
        when(emotionReactionRepository.getEmotionCountOfValue(roomId, -1))
                .thenReturn(5);
        when(emotionReactionRepository.getEmotionCountOfValue(roomId, 0))
                .thenReturn(3);
        when(emotionReactionRepository.getEmotionCountOfValue(roomId, 1))
                .thenReturn(0);
        assertEquals(List.of(5, 3, 0), reactionService.getEmotionCounts(roomLink));
    }


}
