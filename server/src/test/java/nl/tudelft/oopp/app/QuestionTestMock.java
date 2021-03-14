package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.repositories.QuestionRepository;
import nl.tudelft.oopp.app.repositories.UserRepository;
import nl.tudelft.oopp.app.services.QuestionService;
import nl.tudelft.oopp.app.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuestionTestMock {

    @InjectMocks
    private QuestionService questionService;

    @Mock
    private QuestionRepository questionRepository;

    @BeforeEach
    void SetUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllQuestionsSize(){
        when(questionRepository.findAll()).thenReturn(Stream.of(new Question("question1"),new Question("question2")).collect(Collectors.toList()));
        assertEquals(2,questionService.getAllQuestions().size());
    }

    @Test
    public void getSecondQuestion(){
        when(questionRepository.findAll()).thenReturn(Stream.of(new Question("question1"),new Question("question2")).collect(Collectors.toList()));
        assertEquals("question1",questionService.getAllQuestions().get(0));
    }

    @Test
    public void getFirstQuestion(){
        when(questionRepository.findAll()).thenReturn(Stream.of(new Question("question1"),new Question("question2")).collect(Collectors.toList()));
        assertEquals("question2",questionService.getAllQuestions().get(1));
    }
}
