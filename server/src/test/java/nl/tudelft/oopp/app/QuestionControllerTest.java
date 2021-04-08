package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.controllers.QuestionController;
import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.repositories.QuestionsUpdateRepository;
import nl.tudelft.oopp.app.services.QuestionService;

import nl.tudelft.oopp.app.services.QuestionsUpdateService;
import nl.tudelft.oopp.app.services.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the methods in the QuestionController.
 */
@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class QuestionControllerTest {

    @Mock
    private QuestionService questionServiceMock;

    @Mock
    private QuestionsUpdateRepository questionsUpdateRepository;

    @Mock
    private QuestionsUpdateService questionsUpdateService;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private QuestionController questionController;

    String questionIdString;
    long questionId;
    String userIdString;
    long userId;
    String roomLink;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        questionController = new QuestionController(questionServiceMock,
                questionsUpdateService,
                questionsUpdateRepository, roomService);
        questionIdString = "1";
        questionId = 1;
        userId = 1;
        userIdString = "1";
        roomLink = "abc";
    }

    /**
     * test if the getAllQuestions method calls properly calls QuestionService.
     */
    @Test
    public void getAllQuestionsTest() {
        questionController.getAllQuestions();
        verify(questionServiceMock, times(1))
                .getAllQuestions();
    }

    /**
     * test if the getAllQuestions method calls properly calls QuestionService.
     */
    @Test
    public void getAllQuestionsByRoomTest() {
        questionController.getAllQuestions(roomLink);
        verify(questionServiceMock, times(1))
                .getAllQuestionsByRoom(roomLink);
    }

    /**
     * test if the getSingularQuestion method properly calls QuestionService.
     */
    @Test
    public void getSingularQuestionTest() {
        questionController.getSingularQuestion(roomLink, userIdString);
        verify(questionServiceMock, times(1))
                .getSingleQuestion(roomLink, userIdString);
    }

    /**
     * test if the add method call properly calls QuestionService.
     */
    @Test
    public void add_test() {
        Question question = new Question();
        questionController.add(roomLink, userIdString, question);
        verify(questionServiceMock, times(1))
                .addNewQuestion(roomLink, userIdString, question);
    }



    /**
     * test if dismissQuestion method properly calls
     * QuestionService and questionsUpdateRepository.
     */
    @Test
    public void dismissQuestionTest() {
        Question question = new Question();
        question.setId(questionId);
        question.setQuestionText("hello");
        User user = new User();
        Room room = new Room();
        room.setId(1);

        question.setRoom(room);
        question.setUser(user);
        QuestionsUpdate update = new QuestionsUpdate(user, room, -1);
        update.setQuestionText(question.getQuestionText());

        when(questionServiceMock.findByQuestionId(questionId)).thenReturn(question);

        questionController.dismissQuestion(questionId);
        verify(questionServiceMock, times(1)).findByQuestionId(questionId);
        verify(questionServiceMock, times(1)).dismissQuestion(questionId);
        verify(questionsUpdateRepository, times(1)).save(update);
    }

    /**
     * test if dismiss Singular method properly calls QuestionService.
     */
    @Test
    public void dismissSingularTest() {
        questionController.dismissSingular(questionId, userId);
        verify(questionServiceMock, times(1))
                .dismissSingular(questionId, userId);
    }

    /**
     * test if addUpvote method calls properly calls QuestionService.
     */
    @Test
    public void addUpvoteTest() {
        questionController.addUpvote(questionIdString, userIdString);
        verify(questionServiceMock, times(1))
                .addUpvote(questionIdString, userIdString);
    }

    /**
     * test if deleteUpvote method call properly calls QuestionService.
     */
    @Test
    public void deleteUpvoteTest() {
        questionController.deleteUpvote(questionIdString, userIdString);
        verify(questionServiceMock, times(1))
                .deleteUpvote(questionIdString, userIdString);
    }

    /**
     * test if clearQuestions method properly calls QuestionService.
     */
    @Test
    public void clearQuestionTest() {
        questionController.clearQuestions(roomLink);
        verify(questionServiceMock, times(1))
                .clearQuestions(roomLink);
    }

    /**
     * test if editQuestionText method properly calls QuestionService.
     */
    @Test
    public void editQuestionTextTest() {
        String editText = "\"hello\"";
        questionController.editQuestionText(questionIdString, editText);
        verify(questionServiceMock, times(1))
                .editQuestionText(questionId, "hello");
    }

    /**
     * test if setAnswered method properly calls QuestionService.
     * when answeredInClass == true;
     */
    @Test
    public void setAnswered_inClassTrue_Test() {
        String defaultAnswer = "This question was answered during the lecture";

        questionController.setAnswered(questionIdString, userIdString, true);
        verify(questionServiceMock, times(1))
                .setAnswered(defaultAnswer, questionIdString, userIdString, true);
    }

    /**
     * Test if setAnswered method properly calls QuestionService.
     * when answeredInClass == false;
     */
    @Test
    public void setAnswered_inClassFalse_Test() {

        questionController.setAnswered(questionIdString, userIdString, false);
        verify(questionServiceMock, times(1))
                .setAnswered("", questionIdString, userIdString, false);
    }


    /**
     * Test if setAnsweredUpdate method properly calls QuestionService.
     */
    @Test
    public void setAnsweredUpdateTest() {
        Question question = new Question();
        question.setId(questionId);
        question.setQuestionText("hello");
        Room room  = new Room();
        question.setRoom(room);

        User user = new Moderator();
        question.setUser(user);

        when(questionServiceMock.findByQuestionId(questionId))
                .thenReturn(question);


        QuestionsUpdate update = new QuestionsUpdate(user, room, 0);
        update.setQuestionText("hello");

        questionController.setAnsweredUpdate(questionIdString);

        verify(questionServiceMock, times(1))
                .findByQuestionId(questionId);
        verify(questionsUpdateRepository, times(1))
                .save(update);
    }

    /**
     * Test if checkAnswered method properly calls QuestionService.
     */
    @Test
    public void checkAnsweredTest() {
        questionController.checkAnswered(questionIdString, roomLink);
        verify(questionServiceMock, times(1))
                .checkAnswered(questionIdString, roomLink);
    }

    /**
     * Test if exportQuestions method properly calls QuestionService.
     */
    @Test
    public void exportQuestionsTest() {
        questionController.exportQuestions(roomLink);
        verify(questionServiceMock, times(1))
                .exportQuestions(roomLink);
    }

    /**
     * Test if updateOnQuestion method properly calls other methods
     * and returns the correct QuestionsUpdate when it was found.
     */
    @Test
    public void updateOnQuestions_updateNotNull() {
        Room room = new Room();
        room.setId(1);

        User user = new Moderator();
        user.setId(userId);

        QuestionsUpdate update = new QuestionsUpdate(user, room, 0);
        update.setQuestionText("hello");

        when(roomService.getByLink(roomLink)).thenReturn(room);
        when(questionsUpdateService
                .findUpdate(userId, room.getId())).thenReturn(update);

        questionController.updateOnQuestion(userIdString, roomLink);
        verify(roomService, times(1)).getByLink(roomLink);
        verify(questionsUpdateService, times(1))
                .findUpdate(userId, room.getId());
        verify(questionsUpdateService, times(1))
                .deleteUpdate(update.getId(), userId, room.getId());

        QuestionsUpdate actual = questionController.updateOnQuestion(userIdString, roomLink);
        assertEquals(update, actual);
    }

    /**
     * Test if updateOnQuestion method properly calls other methods
     * and returns the correct QuestionsUpdate when it was not found.
     */
    @Test
    public void updateOnQuestion_updateNull_Test() {
        Room room  = new Room();
        room.setId(1);

        when(roomService.getByLink(roomLink)).thenReturn(room);
        when(questionsUpdateService
                .findUpdate(userId, room.getId())).thenReturn(null);

        questionController.updateOnQuestion(userIdString, roomLink);
        verify(roomService, times(1)).getByLink(roomLink);
        verify(questionsUpdateService, times(1))
                .findUpdate(userId, room.getId());
        verify(questionsUpdateService, times(0))
                .deleteUpdate(null, userId, room.getId());
        QuestionsUpdate actual = questionController.updateOnQuestion(userIdString, roomLink);
        assertNull(actual);
    }

    /**
     * Test if setAnswerText method properly calls QuestionService.
     */
    @Test
    public void setAnswerTextTest() {
        String text = "\"hello\"";
        String changedText = "hello";
        questionController.setAnswerText(questionIdString,
                userIdString, "false",text);
        verify(questionServiceMock, times(1))
                .setAnswered(changedText, questionIdString, userIdString, false);
    }


}
