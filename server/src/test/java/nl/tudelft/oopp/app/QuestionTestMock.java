package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.controllers.QuestionController;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.QuestionsUpdate;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.User;
import nl.tudelft.oopp.app.repositories.QuestionRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private User user;
    private Room room;
    private QuestionsUpdate questionsUpdate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new User();
        room = new Room("My room");
        questionsUpdate = new QuestionsUpdate();
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
