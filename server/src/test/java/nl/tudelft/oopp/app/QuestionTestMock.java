package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.controllers.QuestionController;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.QuestionsUpdate;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.User;
import nl.tudelft.oopp.app.repositories.QuestionRepository;
import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.repositories.*;
import nl.tudelft.oopp.app.services.QuestionService;
import nl.tudelft.oopp.app.services.QuestionsUpdateService;
import nl.tudelft.oopp.app.services.RoomService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * test for questionService.
 */
@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class QuestionTestMock {

    @InjectMocks
    private QuestionController questionController;

    @Mock
    private RoomService roomService;

    @Mock
    private QuestionsUpdateService questionsUpdateService;

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private RoomService roomService;
    @Mock
    private UserService userService;
    @Mock
    private UpvoteRepository upvoteRepository;
    @Mock
    private AnswerRepository answerRepository;


    Room room;
    LocalDateTime now;
    String roomLinkModeratorString;
    String roomLinkStudentString;
    User user1;
    User user2;
    Question question1;
    Question question2;
    Question question3;
    Upvote upvote;

    private User user;
    private QuestionsUpdate questionsUpdate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        now = LocalDateTime.now();
        room = new Room("Nice room");
        room.setId(1);
        room.setLinkIdModerator();
        room.newLinkIdStudent();
        roomLinkModeratorString = room.getLinkIdModerator().toString();
        roomLinkStudentString = room.getLinkIdStudent().toString();

        user1 = new Student("Adam", room);
        user1.setId(1);
        user1.setIsModerator(false);
        user2 = new Moderator("Ok", room);
        user2.setIsModerator(true);
        user2.setId(2);

        question1 = new Question("one");
        question1.setId(1);
        question1.setUser(user1);
        question1.setRoom(room);
        question1.setCreatedAt(now);

        question2 = new Question("two");
        question2.setId(2);
        question2.setUser(user2);
        question2.setRoom(room);
        question2.setCreatedAt(now);

        question3 = new Question("three");
        question3.setId(3);
        question3.setAnswered(true);
        question3.setUser(user2);
        question3.setRoom(room);

        upvote = new Upvote(question2, user1);
        upvote.setValue(1);

        question2.setTotalUpVotes(1);

        now = LocalDateTime.now();
    }


    /**
     * tests that the elements of a list returned by getAllQuestionsByRoom
     * have roomId set to 0.
     */
    @Test
    public void getAllQuestionsByRoom_RoomIdZero_Test() {
        when(questionRepository.getQuestionById(1)).thenReturn(question1);
        when(questionRepository.getQuestionById(2)).thenReturn(question1);

        when(questionRepository.findAllIdByRoomLink(room.getLinkIdModerator()))
                .thenReturn(Stream.of(1L, 2L).collect(Collectors.toList()));
        when(roomRepository.findByLink(room.getLinkIdModerator())).thenReturn(room);


        when(questionRepository.findAllByRoomLink(room.getLinkIdModerator()))
                .thenReturn(Stream.of(question1, question2).collect(Collectors.toList()));

        when(roomRepository.getRoomTime(room.getId())).thenReturn(now);

        doNothing().when(questionRepository).updateDuration(anyLong(), anyString());
        questionService.getAllQuestionsByRoom(roomLinkModeratorString);
        assertEquals(0, room.getId());
    }

    /**
     * tests that the elements of a list returned by getAllQuestionsByRoom
     * have userId set to 0.
     */
    @Test
    public void getAllQuestionsByRoom_userIdZero_Test() {
        when(questionRepository.getQuestionById(1)).thenReturn(question1);
        when(questionRepository.getQuestionById(2)).thenReturn(question1);

        when(questionRepository.findAllIdByRoomLink(room.getLinkIdModerator()))
                .thenReturn(Stream.of(1L, 2L).collect(Collectors.toList()));
        when(roomRepository.findByLink(room.getLinkIdModerator())).thenReturn(room);


        when(questionRepository.findAllByRoomLink(room.getLinkIdModerator()))
                .thenReturn(Stream.of(question1, question2).collect(Collectors.toList()));

        when(roomRepository.getRoomTime(room.getId())).thenReturn(now);

        doNothing().when(questionRepository).updateDuration(anyLong(), anyString());
        questionService.getAllQuestionsByRoom(roomLinkModeratorString);
        assertEquals(0, user1.getId());
        assertEquals(0, user2.getId());
    }

    /**
     * tests that the getAllQuestionsByRoom returns the correct list of questions.
     */
    @Test
    public void getAllQuestionsByRoomTest() {
        when(questionRepository.getQuestionById(1)).thenReturn(question1);
        when(questionRepository.getQuestionById(2)).thenReturn(question1);

        when(questionRepository.findAllIdByRoomLink(room.getLinkIdModerator()))
                .thenReturn(Stream.of(1L, 2L).collect(Collectors.toList()));
        when(roomRepository.findByLink(room.getLinkIdModerator())).thenReturn(room);


        when(questionRepository.findAllByRoomLink(room.getLinkIdModerator()))
                .thenReturn(Stream.of(question1, question2).collect(Collectors.toList()));

        when(roomRepository.getRoomTime(room.getId())).thenReturn(now);

        doNothing().when(questionRepository).updateDuration(anyLong(), anyString());
        List<Question> result = questionService.getAllQuestionsByRoom(roomLinkModeratorString);
        assertEquals(List.of(question1, question2), result);
    }

    /**
     * test that the getSingleQuestion method executes as planed
     * and makes correct calls to different methods.
     */
    @Test
    public void getSingleQuestionTest() {
        when(roomService.getByLink(roomLinkModeratorString)).thenReturn(room);
        when(userService.getByID(user1.getId() + "")).thenReturn(user1);

        questionService.getSingleQuestion(roomLinkModeratorString, user1.getId() + "");
        verify(questionRepository, times(1))
                .getSingularQuestion(room.getId(), user1.getId());
    }


    /**
     * verify that the method properly calls the save method from the QuestionRepository
     * when adding a new question.
     */
    @Test
    public void addNewQuestion_callSave_Test() {
        Question question = new Question("test");
        when(userService.getByID(user1.getId() + "")).thenReturn(user1);
        when(roomService.getByLink(roomLinkStudentString)).thenReturn(room);
        questionService.addNewQuestion(roomLinkStudentString, user1.getId() + "", question);
        verify(questionRepository, times(1)).save(question);
        assertEquals(room, question.getRoom());
    }

    /**
     * verify that the method properly set the question room
     * when adding a new question.
     */
    @Test
    public void addNewQuestion_setRoom_Test() {
        Question question = new Question("test");
        when(userService.getByID(user1.getId() + "")).thenReturn(user1);
        when(roomService.getByLink(roomLinkStudentString)).thenReturn(room);
        questionService.addNewQuestion(roomLinkStudentString, user1.getId() + "", question);
        assertEquals(room, question.getRoom());
    }

    /**
     * verify that the method properly set the question user
     * when adding a new question.
     */
    @Test
    public void addNewQuestion_setUser_Test() {
        Question question = new Question("test");
        when(userService.getByID(user1.getId() + "")).thenReturn(user1);
        when(roomService.getByLink(roomLinkStudentString)).thenReturn(room);
        questionService.addNewQuestion(roomLinkStudentString, user1.getId() + "", question);
        assertEquals(user1, question.getUser());
    }

    /**
     * verifies that when the question is dismissed its upVotes are deleted
     * from the database.
     */
    @Test
    public void dismissQuestion_deleteUpvote_Test() {
        when(questionRepository.getOne(question1.getId())).thenReturn(question1);
        questionService.dismissQuestion(question1.getId());
        verify(upvoteRepository, times(1))
                .deleteUpVotesByQuestionId(question1.getId());
        doNothing().when(questionRepository).deleteById(question1.getId());
    }

    /**
     * verifies that when the question is dismissed it's deleted
     * from the database.
     */
    @Test
    public void dismissQuestion_deleteQuestion_Test() {
        when(questionRepository.getOne(question1.getId())).thenReturn(question1);
        questionService.dismissQuestion(question1.getId());
        doNothing().when(upvoteRepository).deleteUpVotesByQuestionId(question1.getId());
        verify(questionRepository, times(1))
                .deleteById(question1.getId());
    }

    /**
     * test that the dismissSingular method executes as planned and calls proper
     * questionRepository methods.
     */
    @Test
    public void dismissSingularTest() {
        when(questionRepository.getOne(question1.getId())).thenReturn(question1);
        questionService.dismissSingular(question1.getId(), user1.getId());
        verify(questionRepository, times(1))
                .deleteSingular(question1.getId(), question1.getUser().getId());
    }

    /**
     * verifies that the addUpvote method properly saves the upvote in the database
     * when the user is a moderator.
     */
    @Test
    public void addUpvote_Moderator_Save_Test() {
        //user2 upvote question1

        when(userService.getByID(user2.getId() + ""))
                .thenReturn(user2);
        when(questionRepository.getOne(question1.getId()))
                .thenReturn(question1);
        Upvote upvote = new Upvote(question1, user2);
        upvote.setValue(10);
        when(questionRepository.getUpVoteCount(question1.getId()))
                .thenReturn(question1.getTotalUpVotes());

        questionService.addUpvote(question1.getId() + "", user2.getId() + "");
        verify(upvoteRepository, times(1)).save(upvote);
    }

    /**
     * verifies that the addUpvote method properly saves the upvote in the database.
     * when the user is a student.
     */
    @Test
    public void addUpvote_Student_Save_Test() {
        //user1 upvote question3

        when(userService.getByID(user1.getId() + ""))
                .thenReturn(user1);
        when(questionRepository.getOne(question3.getId()))
                .thenReturn(question3);
        Upvote upvote = new Upvote(question3, user1);
        upvote.setValue(1);
        when(questionRepository.getUpVoteCount(question3.getId()))
                .thenReturn(question3.getTotalUpVotes());

        questionService.addUpvote(question3.getId() + "", user1.getId() + "");
        verify(upvoteRepository, times(1)).save(upvote);
    }

    /**
     * verifies that the addUpvote method properly updates total question upvote
     * whe the user is a moderator.
     */
    @Test
    public void addUpvote_Moderator_TotalUpvote_Test() {
        //user2 upvote question1

        when(userService.getByID(user2.getId() + ""))
                .thenReturn(user2);
        when(questionRepository.getOne(question1.getId()))
                .thenReturn(question1);
        Upvote upvote = new Upvote(question1, user2);
        upvote.setValue(10);
        when(questionRepository.getUpVoteCount(question1.getId()))
                .thenReturn(question1.getTotalUpVotes());

        questionService.addUpvote(question1.getId() + "", user2.getId() + "");
        assertEquals(10, question1.getTotalUpVotes());
    }

    /**
     * verifies that the addUpvote method properly updates total question upvote
     * whe the user is a student.
     */
    @Test
    public void addUpvote_Student_TotalUpvote_Test() {
        //user1 upvote question1

        when(userService.getByID(user1.getId() + ""))
                .thenReturn(user1);
        when(questionRepository.getOne(question3.getId()))
                .thenReturn(question3);
        Upvote upvote = new Upvote(question3, user1);
        upvote.setValue(1);
        when(questionRepository.getUpVoteCount(question3.getId()))
                .thenReturn(question3.getTotalUpVotes());

        questionService.addUpvote(question3.getId() + "", user1.getId() + "");
        assertEquals(1, question3.getTotalUpVotes());
    }


    /**
     * tests if the upvote is deleted properly from the database.
     */
    @Test
    public void deleteUpvote_delete_Test() {
        //delete upvote user1 for question2

        when(questionRepository.getOne(question2.getId())).thenReturn(question2);
        when(upvoteRepository.findUpvoteByUserAndQuestion(user1.getId(), question2.getId()))
                .thenReturn(upvote);
        when(questionRepository.getUpVoteCount(question2.getId()))
                .thenReturn(question2.getTotalUpVotes());
        questionService.deleteUpvote(question2.getId() + "", user1.getId() + "");
        verify(upvoteRepository, times(1)).deleteById(upvote.getId());

    }

    /**
     * tests if the total number of upvote for a question is corrected
     * after deleting an upvote.
     */
    @Test
    public void deleteUpvote_TotalUpvote_Test() {
        //delete upvote user1 for question2

        when(questionRepository.getOne(question2.getId())).thenReturn(question2);
        when(upvoteRepository.findUpvoteByUserAndQuestion(user1.getId(), question2.getId()))
                .thenReturn(upvote);
        when(questionRepository.getUpVoteCount(question2.getId()))
                .thenReturn(question2.getTotalUpVotes());
        questionService.deleteUpvote(question2.getId() + "", user1.getId() + "");
        assertEquals(0, question2.getTotalUpVotes());

    }

    /**
     * verifies if clearQuestions properly deletes all answers
     * related to the retrieved questions.
     */
    @Test
    public void clearQuestions_deleteAnswers_Test() {
        when(roomService.getByLinkModerator(roomLinkModeratorString))
                .thenReturn(room);
        when(questionRepository.findAllByRoomLink(room.getLinkIdModerator()))
                .thenReturn(Stream.of(question1, question2, question3)
                        .collect(Collectors.toList()));

        questionService.clearQuestions(roomLinkModeratorString);

        verify(answerRepository, times(1))
                .deleteByQuestionID(question1.getId());
        verify(answerRepository, times(1))
                .deleteByQuestionID(question2.getId());
        verify(answerRepository, times(1))
                .deleteByQuestionID(question3.getId());
    }

    /**
     * verifies if clearQuestions properly calls a method in question repository
     * to clear all the questions.
     */
    @Test
    public void clearQuestions_clearQuestions_Test() {
        when(roomService.getByLinkModerator(roomLinkModeratorString))
                .thenReturn(room);
        when(questionRepository.findAllByRoomLink(room.getLinkIdModerator()))
                .thenReturn(Stream.of(question1, question2, question3)
                        .collect(Collectors.toList()));

        questionService.clearQuestions(roomLinkModeratorString);

        verify(questionRepository, times(1))
                .clearQuestions(room.getId());
    }


    /**
     * test that findUserByQuestionId method correctly calls methods from
     * questionRepository and questionService.
     */
    @Test
    public void findUserByQuestionIdTest() {
        when(questionRepository.getUserByQuestionId(question1.getId()))
                .thenReturn(question1.getUser());
        questionService.findUserByQuestionId(question1.getId() + "");
        verify(questionRepository, times(1))
                .getUserByQuestionId(question1.getId());
    }


    /**
     * test that the method setAnswered calls the right method for saving
     * the answer for the question.
     */
    @Test
    public void setAnswered_questionSetAnswer_Test() {
        when(userService.getByID(user2.getId() + ""))
                .thenReturn(user2);
        when(questionRepository.getOne(question1.getId()))
                .thenReturn(question1);
        questionService.setAnswered("answer", question1.getId() + "",
                user2.getId() + "", true);
        verify(questionRepository, times(1))
                .setAnswer(question1.getId(), "answer");
    }

    /**
     * test that the method setAnswered calls the right methods when
     * the answer to be set has answerType true.
     */
    @Test
    public void setAnswered_answerTypeTrue_Test() {
        when(userService.getByID(user2.getId() + ""))
                .thenReturn(user2);
        when(questionRepository.getOne(question1.getId()))
                .thenReturn(question1);

        Answer answer = new Answer("answer", question1,
                (Moderator) user2, true);
        questionService.setAnswered("answer", question1.getId() + "",
                user2.getId() + "", true);
        verify(questionRepository, times(1))
                .updateAnsweredStatus(question1.getId(), true);
        verify(answerRepository, times(1)).save(answer);
    }

    /**
     * test that the method setAnswered calls the right methods when
     * the answer to be set has answerType false.
     */
    @Test
    public void setAnswered_answerTypeFalse_Test() {
        when(userService.getByID(user2.getId() + ""))
                .thenReturn(user2);
        when(questionRepository.getOne(question1.getId()))
                .thenReturn(question1);

        Answer answer = new Answer("answer", question1,
                (Moderator) user2, false);
        questionService.setAnswered("answer", question1.getId() + "",
                user2.getId() + "", false);
        verify(answerRepository, times(1))
                .deleteByQuestionID(question1.getId());
        verify(answerRepository, times(1)).save(answer);
    }

    /**
     * Test that method checkAnswered executes properly and calls the method
     * from questionRepository correctly.
     */
    @Test
    public void checkAnsweredTest() {
        when(roomService.getByLink(roomLinkModeratorString))
                .thenReturn(room);
        questionService.checkAnswered(question3.getId() + "",
                roomLinkModeratorString);
        verify(questionRepository, times(1))
                .checkAnswered(question3.getId(), room.getId());
    }

    /**
     * test if exportQuestions correctly finds a room from a link
     * and returns the output of getQuestionsAndAnswers.
     */
    @Test
    public void testExportQuestions() {
        when(roomService.getByLinkModerator(roomLinkModeratorString))
                .thenReturn(room);
        when(questionRepository.getAllQuestionIds(room.getId()))
                .thenReturn(List.of());
        when(roomRepository.getRoomName(room.getId())).thenReturn(room.getName());

        String expected = "\nQuestions and Answers from Room: Nice room 1\n\n";

        assertEquals(expected, questionService.exportQuestions(roomLinkModeratorString));

    }

    /**
     * tests that the methods output is correct when
     * there is one unanswered question in the room.
     */
    @Test
    public void getQuestionsAndAnswers_noAnswer_Test() {
        question1.setCreatedAt(now);
        when(questionRepository.getAllQuestionIds(room.getId()))
                .thenReturn(Stream.of(question1.getId()).collect(Collectors.toList()));
        when(roomRepository.getRoomName(room.getId())).thenReturn(room.getName());
        when(questionRepository.getQuestionById(question1.getId())).thenReturn(question1);
        when(roomRepository.getRoomTime(room.getId())).thenReturn(now.minusMinutes(1));
        question1.setDuration("0:1:0");
        when(questionRepository.getQuestionText(question1.getId()))
                .thenReturn(question1.getQuestionText());
        when(answerRepository.getAllAnswerIds(question1.getId())).thenReturn(List.of());


        String expected = "\nQuestions and Answers from Room: Nice room 1\n\n"
                + "Question: \n0:1:0: one\nThis question was not answered yet\n\n";

        assertEquals(expected, questionService.getQuestionsAndAnswers(room));
    }

    /**
     * tests that the methods output is correct when
     * there is one answered question in the room.
     */
    @Test
    public void getQuestionsAndAnswers_Answer_Test() {
        question3.setCreatedAt(now);
        Answer answer = new Answer("answer", question3, (Moderator) user2, false);
        answer.setId(1);
        answer.setCreatedAt(now.plusSeconds(30));

        when(questionRepository.getAllQuestionIds(room.getId()))
                .thenReturn(Stream.of(question3.getId()).collect(Collectors.toList()));
        when(roomRepository.getRoomName(room.getId())).thenReturn(room.getName());
        when(questionRepository.getQuestionById(question3.getId())).thenReturn(question3);
        when(roomRepository.getRoomTime(room.getId())).thenReturn(now.minusMinutes(1));
        question3.setDuration("0:1:0");
        when(questionRepository.getQuestionText(question3.getId()))
                .thenReturn(question3.getQuestionText());

        when(answerRepository.getAllAnswerIds(question3.getId()))
                .thenReturn(List.of(answer.getId()));
        when(answerRepository.getAnswerById(answer.getId())).thenReturn(answer);
        when(answerRepository.getAnswerText(answer.getId())).thenReturn(answer.getAnswerText());
        when(roomRepository.getRoomTime(room.getId())).thenReturn(now.minusMinutes(1));

        String expected = "\nQuestions and Answers from Room: Nice room 1\n\n"
                + "Question: \n0:1:0: three\nAnswers:\n0:1:30: answer\n\n";

        assertEquals(expected, questionService.getQuestionsAndAnswers(room));
    }

    /**
     * test that in the getUpVotes method
     * the correct total upvote is returned.
     */
    @Test
    public void getUpVotesTest() {
        when(roomService.getByLink(roomLinkModeratorString)).thenReturn(room);
        when(upvoteRepository.getUpVotes(question2.getId(), room.getId()))
                .thenReturn(question2.getTotalUpVotes());
        assertEquals(1, questionService
                .getUpVotes(question2.getId(), room.getLinkIdModerator()));
    }

    /**
     * test that in the getUpVotes method
     * when the totalUpVotes for a question is null then 0 is returned.
     */
    @Test
    public void getUpVotes_Null_Test() {
        question1.setTotalUpVotes(null);
        when(roomService.getByLink(roomLinkModeratorString)).thenReturn(room);
        when(upvoteRepository.getUpVotes(question1.getId(), room.getId()))
                .thenReturn(question1.getTotalUpVotes());
        assertEquals(0, questionService
                .getUpVotes(question1.getId(), room.getLinkIdModerator()));
        user = new User();
        room = new Room("My room");
        questionsUpdate = new QuestionsUpdate();
    }

    /**
     * tests that the method findByQuestionId properly calls
     * questionRepository method.
     */
    @Test
    public void findByQuestionIdTest() {
        when(questionRepository.findById(question1.getId()))
                .thenReturn(java.util.Optional.ofNullable(question1));
        assertEquals(question1, questionService.findByQuestionId(question1.getId()));

    }

    /**
     * test that the getAllAnsweredQuestions returns the correct list.
     */
    @Test
    public void getAllAnsweredQuestions_Username_test() {
        when(questionRepository.findAllAnsweredByRoomLink(room.getLinkIdStudent()))
                .thenReturn(Stream.of(question3).collect(Collectors.toList()));
        List<Question> actual = questionService.getAllAnsweredQuestions(roomLinkStudentString);
        assertEquals(List.of(question3), actual);
    }

    /**
     * test that the getAllAnsweredQuestions sets the room id of all retrieved questions to 0.
     */
    @Test
    public void getAllAnsweredQuestions_Room_test() {
        when(questionRepository.findAllAnsweredByRoomLink(room.getLinkIdStudent()))
                .thenReturn(Stream.of(question3).collect(Collectors.toList()));
        questionService.getAllAnsweredQuestions(roomLinkStudentString);
        assertEquals(0, question3.getRoom().getId());
    }

    /**
     * test that the getAllAnsweredQuestions sets the user id of all retrieved questions to 0.
     */
    @Test
    public void getAllAnsweredQuestions_User_test() {
        when(questionRepository.findAllAnsweredByRoomLink(room.getLinkIdStudent()))
                .thenReturn(Stream.of(question3).collect(Collectors.toList()));
        questionService.getAllAnsweredQuestions(roomLinkStudentString);
        assertEquals(0, question3.getUser().getId());
    }

    /**
     * tests that the getTimeOfQuestions correctly updates it in the database.
     */
    @Test
    public void getTimeOfQuestionsTest() {
        question1.setCreatedAt(now);
        when(roomRepository.getRoomTime(room.getId()))
                .thenReturn(now.minusMinutes(1));
        when(questionRepository.getQuestionById(question1.getId())).thenReturn(question1);

        questionService.getTimeOfQuestions(List.of(question1.getId()), room.getId());
        verify(questionRepository, times(1))
                .updateDuration(question1.getId(), "0:1:0");
    }

    /**
     * tests that the getTimeOfAnswers correctly sets the duration
     * of an answer and updates it in the database.
     */
    @Test
    public void getTimeOfAnswersTest() {
        Answer answer = new Answer("answer", question1,
                (Moderator) user2, false);
        answer.setId(1);
        answer.setCreatedAt(now);
        when(roomRepository.getRoomTime(room.getId()))
                .thenReturn(now.minusMinutes(1));
        when(answerRepository.getAnswerById(answer.getId())).thenReturn(answer);

        questionService.getTimeOfAnswers(List.of(1L), room.getId());
        assertEquals("0:1:0", answer.getDuration());
        verify(answerRepository, times(1))
                .updateDuration(answer.getId(), "0:1:0");
    }

    /**
     * tests of the formatDuration method correctly transforms the parameters
     * into a duration string.
     */
    @Test
    public void formatDurationTest() {
        String expected = "0:1:0";

        String actual = questionService
                .formatDuration(now.minusMinutes(1),
                now);

        assertEquals(expected, actual);
    }

    /**
     * test if the method setAge correctly sets age of the question.
     */
    @Test
    public void setAgeTest() {
        questionService.setAge(now.minusMinutes(1L), question1);
        verify(questionRepository, times(1))
                .updateAge(question1.getId(), "60");
    }


    /**
     * check the if the size of the returned object is the same as the expected.
     */
    @Test
    public void getAllQuestionsSize() {
        when(questionRepository.findAll())
                .thenReturn(Stream.of(
                        new Question("question1"),
                        new Question("question2"))
                        .collect(Collectors.toList()));
        assertEquals(2, questionService.getAllQuestions().size());
    }

    /**
     * check if the returned question text is the same as the intended.
     */
    @Test
    public void getSecondQuestion() {
        when(questionRepository.findAll())
                .thenReturn(Stream.of(
                        new Question("question1"),
                        new Question("question2"))
                        .collect(Collectors.toList()));
        assertEquals("question2", questionService.getAllQuestions().get(1).getQuestionText());
    }

    /**
     * check if the returned question text is the same as the intended.
     */
    @Test
    public void getFirstQuestion() {
        when(questionRepository.findAll())
                .thenReturn(Stream.of(
                        new Question("question1"),
                        new Question("question2"))
                        .collect(Collectors.toList()));
        assertEquals("question1", questionService.getAllQuestions().get(0).getQuestionText());
    }

    /**
     * tests service method for editing question text.
     */
    @Test
    public void editQuestionTest() {
        Question q = new Question("this will be edited");
        when(questionRepository.getOne(q.getId())).thenReturn(q);
        doNothing().when(questionRepository).editQuestionText(q.getId(), "This is edited");

        questionService.editQuestionText(q.getId(), "This is edited");
        q.setQuestionText("This is edited");
        assertEquals("This is edited", questionRepository.getOne(q.getId()).getQuestionText());
    }

    /**
     * This method tests that the returned update is as the expected and also the update
     * is deleted.
     */
    @Test
    public void testUpdateOnQuestion() {


        when(roomService.getByLink(room.getLinkIdStudent().toString()))
                .thenReturn(room);

        when(questionsUpdateService.findUpdate(user.getId(), room.getId()))
                .thenReturn(questionsUpdate);

        assertEquals(questionsUpdate,
                questionController.updateOnQuestion(String.valueOf(user.getId()),
                        String.valueOf(room.getLinkIdStudent())));

        verify(questionsUpdateService)
                .deleteUpdate(questionsUpdate.getId(), user.getId(), room.getId());
    }

    /**
     * This method tests that there is no updates and returns null.
     */
    @Test
    public void testUpdateOnQuestionNull() {

        when(roomService.getByLink(room.getLinkIdStudent().toString()))
                .thenReturn(room);
        when(questionsUpdateService.findUpdate(user.getId(), room.getId()))
                .thenReturn(null);
        assertNull(questionController.updateOnQuestion(String.valueOf(user.getId()),
                String.valueOf(room.getLinkIdStudent())));

    }
}
