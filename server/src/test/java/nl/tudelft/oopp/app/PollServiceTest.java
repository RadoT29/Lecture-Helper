package nl.tudelft.oopp.app;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.repositories.PollAnswerRepository;
import nl.tudelft.oopp.app.repositories.PollOptionRepository;
import nl.tudelft.oopp.app.repositories.PollRepository;
import nl.tudelft.oopp.app.services.PollService;
import nl.tudelft.oopp.app.services.RoomService;
import nl.tudelft.oopp.app.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class PollServiceTest {

    @InjectMocks
    PollService pollService;

    @Mock
    private PollRepository pollRepository;
    @Mock
    private PollOptionRepository pollOptionRepository;
    @Mock
    private PollAnswerRepository pollAnswerRepository;
    @Mock
    private RoomService roomService;
    @Mock
    private UserService userService;

    @Spy
    private PollService pollServiceSpied;

    private Room room;
    private String roomLinkString;
    private UUID roomLink;
    private Poll poll1;
    private Long poll1Id;
    private Poll poll2;

    private PollOption pollOption1;
    private PollOption pollOption2;
    private PollOption pollOption3;

    private PollAnswer pollAnswer1;
    private PollAnswer pollAnswer2;

    private List<Poll> polls;
    private List<PollAnswer> answers;

    private User user;
    private Long userId;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        pollServiceSpied = spy(pollService);

        //Creates a room
        room = new Room("room name");
        roomLink = room.getLinkIdModerator();
        roomLinkString = roomLink.toString();

        //Creates a user
        user = new Student();
        userId = user.getId();

        //Creates a poll
        poll1 = new Poll(room);
        poll1.setQuestion("abc?");
        poll1Id = poll1.getId();

        //Creates a second poll
        poll2 = new Poll();

        //Adds all polls to a list
        polls = new ArrayList<>();
        polls.add(poll1);
        polls.add(poll2);

        //Creates options
        pollOption1 = new PollOption();
        pollOption1.setId(0);
        pollOption1.setCorrect(true);
        pollOption1.setOptionText("a");

        pollOption2 = new PollOption();
        pollOption2.setId(1);
        pollOption2.setCorrect(false);
        pollOption2.setOptionText("b");

        pollOption3 = new PollOption();
        pollOption3.setCorrect(false);
        pollOption3.setOptionText("c");

        //Sets options for poll 1
        List<PollOption> optionsForPoll1 = new ArrayList<>();
        optionsForPoll1.add(pollOption1);
        optionsForPoll1.add(pollOption2);
        poll1.setPollOptions(optionsForPoll1);

        //Sets options for poll 1
        List<PollOption> optionsForPoll2 = new ArrayList<>();
        optionsForPoll2.add(pollOption3);
        poll2.setPollOptions(optionsForPoll2);

        //Set answers for poll 1
        answers = new ArrayList<>();
        pollAnswer1 = new PollAnswer((Student) user, pollOption1,true);
        pollAnswer2 = new PollAnswer((Student) user, pollOption2, false);
        answers.add(pollAnswer1);
        answers.add(pollAnswer2);
    }

    /**
     * Verifies that auxiliary method is called correctly.
     */
    @Test
    void getPollsTestCallsAux() {
        UUID roomLink = UUID.fromString(roomLinkString);
        when(pollRepository.findAllByRoomLink(roomLink)).thenReturn(polls);
        pollServiceSpied.getPolls(roomLinkString);
        verify(pollServiceSpied,times(1)).calculateScore(poll1);
        verify(pollServiceSpied,times(1)).calculateScore(poll2);
    }

    /**
     * Verifies that scores for options are calculated correctly.
     */
    @Test
    public void calculateScoreTest() {
        int countOption1 = 10;
        int countOption2 = 2;
        int markedOption1 = 5;
        int markedOption2 = 1;

        when(pollAnswerRepository.countByPollOptionId(pollOption1.getId()))
                .thenReturn(countOption1);
        when(pollAnswerRepository.countByPollOptionId(pollOption2.getId()))
                .thenReturn(countOption2);

        when(pollAnswerRepository.countMarkedByPollOptionId(pollOption1.getId()))
                .thenReturn(markedOption1);
        when(pollAnswerRepository.countMarkedByPollOptionId(pollOption2.getId()))
                .thenReturn(markedOption2);


        double expectedRate1 = ((float)markedOption1 / countOption1);
        double expectedRate2 = (1 - (float)markedOption2 / countOption2);

        pollService.calculateScore(poll1);
        assertEquals(expectedRate1, pollOption1.getScoreRate());
        assertEquals(expectedRate2, pollOption2.getScoreRate());
    }


    /**
     * Verifies that score is -1 since no one answered.
     */
    @Test
    public void calculateScoreTestNegative() {
        int countOption3 = 0;
        int markedOption3 = 0;

        when(pollAnswerRepository.countByPollOptionId(pollOption3.getId()))
                .thenReturn(countOption3);

        when(pollAnswerRepository.countMarkedByPollOptionId(pollOption3.getId()))
                .thenReturn(markedOption3);

        double expectedRate3 = -1;

        pollService.calculateScore(poll2);
        assertEquals(expectedRate3, pollOption3.getScoreRate());
    }

    /**
     * Verifies that isModerator is determined correctly: true.
     */
    @Test
    void isModeratorTrue() {
        when(roomService.getByLinkModerator(roomLinkString)).thenReturn(room);
        assertTrue(pollService.isModerator(roomLinkString));
    }


    /**
     * Verifies that isModerator is determined correctly: false.
     */
    @Test
    void isModeratorFalse() {
        when(roomService.getByLinkModerator(roomLinkString)).thenReturn(null);
        assertFalse(pollService.isModerator(roomLinkString));
    }


    /**
     * Verifies that no action is taken since not a moderator.
     */
    @Test
    void createPollTestNotMod() {

        when(roomService.getByLinkModerator(roomLinkString)).thenReturn(null);
        assertEquals(0,pollService.createPoll(roomLinkString));
    }


    /**
     * Verifies that a poll is created since user is a mod.
     */
    @Test
    void createPollTestMod() {

        when(roomService.getByLinkModerator(roomLinkString)).thenReturn(room);
        when(roomService.getByLink(roomLinkString)).thenReturn(room);

        assertEquals(poll1Id,pollService.createPoll(roomLinkString));

    }


    /**
     * Verifies that options are deleted for room.
     */
    @Test
    void clearPollOptions() {

        pollService.clearPollOptions(poll1Id);
        verify(pollOptionRepository,times(1)).deleteOptionsFromRoom(poll1Id);
    }

    /**
     * Verifies that a poll option is added.
     */
    @Test
    void addPollOption() {

        when(pollRepository.findById(poll1Id)).thenReturn(Optional.of(poll1));

        pollService.addPollOption(poll1Id,"",true);
        PollOption pollOption = new PollOption(poll1,"", true);
        verify(pollOptionRepository,times(1)).save(pollOption);
    }

    /**
     * Verifies that no action is taken since user is not a mod.
     */
    @Test
    void updateAndOpenPollNoMod() {
        when(roomService.getByLinkModerator(roomLinkString)).thenReturn(null);
        pollService.updateAndOpenPoll(roomLinkString,poll1Id,poll1);
        verifyNoInteractions(pollRepository);
    }

    /**
     * Verifies that all options are cleared and that the poll is opened.
     */
    @Test
    void updateAndOpenPollMod() {

        when(roomService.getByLinkModerator(roomLinkString)).thenReturn(room);
        when(pollRepository.findById(poll1Id)).thenReturn(Optional.of(poll1));

        pollService.updateAndOpenPoll(roomLinkString, poll1Id, poll1);

        verify(pollOptionRepository,times(1)).deleteOptionsFromRoom(poll1Id);
        verify(pollRepository,times(1)).updateAndOpenPoll(poll1Id,poll1.getQuestion());
    }

    /**
     * Verifies that no action is taken since user is not a mod.
     */
    @Test
    void finishPollTestNoMod() {
        when(roomService.getByLinkModerator(roomLinkString)).thenReturn(null);
        pollService.finishPoll(roomLinkString,poll1Id);
        verifyNoInteractions(pollRepository);
    }

    /**
     * Verifies that a poll becomes finished.
     */
    @Test
    void finishPollTestMod() {
        when(roomService.getByLinkModerator(roomLinkString)).thenReturn(room);
        pollService.finishPoll(roomLinkString,poll1Id);
        verify(pollRepository,times(1)).finishPoll(poll1Id);
    }

    /**
     * Verifies that taken answers are correct.
     */
    @Test
    void getAnswersTest() {
        when(pollAnswerRepository.findAnswersByUserAndPollId(poll1Id,userId)).thenReturn(answers);
        assertEquals(answers,pollService.getAnswers(poll1Id,userId));
    }

    /**
     * Verifies that an answer gets updated correctly.
     */
    @Test
    void updateAnswersTest() {
        pollService.updateAnswer(userId,true);
        verify(pollAnswerRepository,times(1)).updateAnswer(userId,true);
    }

    /**
     * Verifies that answers get updated correctly.
     */
    @Test
    void createAnswersTestUpdate() {

        when(pollAnswerRepository.findAnswerByUserAndOptionId(pollOption1.getId(),userId))
                .thenReturn(pollAnswer1);
        when(pollAnswerRepository.findAnswerByUserAndOptionId(pollOption2.getId(),userId))
                .thenReturn(pollAnswer2);
        when(userService.getByID(String.valueOf(userId))).thenReturn(user);
        when(pollOptionRepository.findById(pollOption1.getId()))
                .thenReturn(Optional.of(pollOption1));
        when(pollOptionRepository.findById(pollOption2.getId()))
                .thenReturn(Optional.of(pollOption2));

        pollService.createAnswers(userId,poll1);


        verify(pollAnswerRepository,times(1))
                .updateAnswer(pollAnswer1.getId(), pollAnswer1.isMarked());
        verify(pollAnswerRepository,times(1))
                .updateAnswer(pollAnswer2.getId(), pollAnswer2.isMarked());

    }

    /**
     * Verifies that new answers get created correctly.
     */
    @Test
    void createAnswersTestNew() {

        when(pollAnswerRepository.findAnswerByUserAndOptionId(pollOption1.getId(),userId))
                .thenReturn(null);
        when(pollAnswerRepository.findAnswerByUserAndOptionId(pollOption2.getId(),userId))
                .thenReturn(null);

        when(userService.getByID(String.valueOf(userId)))
                .thenReturn(user);

        when(pollOptionRepository.findById(pollOption1.getId()))
                .thenReturn(Optional.of(pollOption1));
        when(pollOptionRepository.findById(pollOption2.getId()))
                .thenReturn(Optional.of(pollOption2));

        pollService.createAnswers(userId,poll1);


        verify(pollAnswerRepository,times(1)).saveAll(answers);
    }







}
