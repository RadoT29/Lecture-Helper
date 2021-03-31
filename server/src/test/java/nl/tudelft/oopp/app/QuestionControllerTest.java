package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.controllers.QuestionController;
import nl.tudelft.oopp.app.services.QuestionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.mockito.Mockito.*;

/**
 * This class tests the methods in the QuestionController.
 */
@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class QuestionControllerTest {

    @Mock
    private QuestionService questionServiceMock;

    private QuestionController questionController;

    String questionId;

    String userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        questionController = new QuestionController(questionServiceMock);
        questionId = "1";
        userId = "1";
    }

    /**
     * This test checks if setAnswerText() executes what is expected
     * and calls the Question service.
     */
    @Test
    public void answerTextTest() {
        String answerText = "'Test'";
        String answerTextService = "Test";

        questionController.setAnswerText(questionId, userId, answerText);
        verify(questionServiceMock,times(1))
                .setAnswered(answerTextService,
                        questionId, userId, false);
    }

    /**
     * This test checks if setAnswered() is executed as planned
     * and properly calls the Question service.
     */
    @Test
    public void setAnsweredTest() {

        questionController.setAnswered(questionId,userId, true);
        verify(questionServiceMock,times(1))
                .setAnswered("This question was answered during the lecture",
                        questionId, userId, true);
    }

}
