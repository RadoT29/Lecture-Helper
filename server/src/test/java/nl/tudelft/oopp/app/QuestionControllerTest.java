package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.controllers.QuestionController;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.services.QuestionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.web.bind.annotation.PathVariable;

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

    String questionIdString;
    long questionId;
    String userIdString;
    long userId;
    String roomLink;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        questionController = new QuestionController(questionServiceMock);
        questionIdString = "1";
        questionId = 1;
        userId = 1;
        userIdString = "1";
        roomLink = "abc";
    }

    /**
     * test if the getAllQuestions method calls correct service method.
     */
    @Test
    public void getAllQuestionsTest() {
        questionController.getAllQuestions();
        verify(questionServiceMock, times(1))
                .getAllQuestions();
    }
    /**
     * test if the getAllQuestions method calls correct service method.
     */
    @Test
    public void getAllQuestionsByRoomTest() {
        questionController.getAllQuestions(roomLink);
        verify(questionServiceMock, times(1))
                .getAllQuestionsByRoom(roomLink);
    }

    /**
     * test if the getSingularQuestion method calls correct service method.
     */
    @Test
    public void getSingularQuestionTest() {
        questionController.getSingularQuestion(roomLink, userIdString);
        verify(questionServiceMock, times(1))
                .getSingleQuestion(roomLink, userIdString);
    }

    /**
     * test if the add method call correct service method.
     */
    @Test
    public void add_test() {
        Question question = new Question();
        questionController.add(roomLink, userIdString, question);
        verify(questionServiceMock, times(1))
                .addNewQuestion(roomLink, userIdString, question);
    }


    //Test dismissQuestion
//    @Test
//    public void

    /**
     * test if dismiss Singular method calls correct service method.
     */
    @Test
    public void dismissSingularTest() {
        questionController.dismissSingular(questionId, userId);
        verify(questionServiceMock, times(1))
                .dismissSingular(questionId, userId);
    }

    /**
     * test if addUpvote method calls correct service method.
     */
    @Test
    public void addUpvoteTest() {
        questionController.addUpvote(questionIdString, userIdString);
        verify(questionServiceMock, times(1))
                .addUpvote(questionIdString, userIdString);
    }

    /**
     * test if deleteUpvote method call correct service method.
     */
    @Test
    public void deleteUpvoteTest() {
        questionController.deleteUpvote(questionIdString, userIdString);
        verify(questionServiceMock, times(1))
                .deleteUpvote(questionIdString, userIdString);
    }

    /**
     * test if clearQuestions method call correct service method.
     */
    @Test
    public void clearQuestionTest() {
        questionController.clearQuestions(roomLink);
        verify(questionServiceMock, times(1))
                .clearQuestions(roomLink);
    }

  //test sendAllQuestionsAsync
    //test sendAllAnsweredQuestionsAsync

    /**
     * test if editQuestionText method calls correct service method.
     */
    @Test
    public void editQuestionTextTest() {
        String editText = "\"hello\"";
        questionController.editQuestionText(questionIdString, editText);
        verify(questionServiceMock, times(1))
                .editQuestionText(questionId, "hello");
    }

    /**
     * test if setAnswered_inClassTrue
     */
    @Test
    public void setAnswered_inClassTrue_Test() {
        String defaultAnswer = "This question was answered during the lecture";

        questionController.setAnswered(questionIdString, userIdString, true);
        verify(questionServiceMock, times(1))
                .setAnswered(defaultAnswer, questionIdString, userIdString, true);
    }
    /**
     * This test checks if setAnswerText() executes what is expected
     * and calls the Question service.
     */
    @Test
    public void answerTextTest() {
        String answerText = "'Test'";
        String answerTextService = "Test";

        questionController.setAnswerText(questionIdString, userIdString, answerText);
        verify(questionServiceMock,times(1))
                .setAnswered(answerTextService,
                        questionIdString, userIdString, false);
    }

    /**
     * This test checks if setAnswered() is executed as planned
     * and properly calls the Question service.
     */
    @Test
    public void setAnsweredTest() {

        questionController.setAnswered(questionIdString, userIdString, true);
        verify(questionServiceMock,times(1))
                .setAnswered("This question was answered during the lecture",
                        questionIdString, userIdString, true);
    }

}
