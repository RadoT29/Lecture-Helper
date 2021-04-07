package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.repositories.QuestionRepository;
import nl.tudelft.oopp.app.services.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * test for questionService.
 */
@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class QuestionTestMock {

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
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
}
