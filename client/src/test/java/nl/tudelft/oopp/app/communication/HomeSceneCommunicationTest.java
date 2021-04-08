package nl.tudelft.oopp.app.communication;

import nl.tudelft.oopp.app.exceptions.NoSuchRoomException;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class HomeSceneCommunicationTest {
    private ClientAndServer mockServer;

    /**Start the mock server before each test.
     */
    @BeforeEach
    public void startMockServer() {
        mockServer = startClientAndServer(8080);
    }

    /**Close the mock server after each test so they are completely isolated.
     * Clear the sessions singleton for the same reason.
     */
    @AfterEach
    public void stopMockServer() {
        Session.clearSessionTest();
        mockServer.stop();
    }

    /**
     * Test if HomeSceneCommunication.postQuestion
     * sends a post question request to the right path.
     */
    @Test
    void shouldSendQuestionRequest() {
        String roomLink = UUID.randomUUID().toString();
        String roomName = "roomName456";
        String userId = UUID.randomUUID().toString();

        Session session = Session.getInstance(roomLink, roomName, userId, true);
        HomeSceneCommunication.postQuestion(new Question());

        String path = "/questions/" + roomLink + '/' + userId;
        mockServer.verify(request()
                .withMethod("POST")
                .withPath(path));
    }

    /**Method to set the mock server for the proper request/response.
     */
    void mockGetQuestions(
            List<Question> questions, String roomLink, String roomName, String userId
    ) {
        System.out.println("/questions/refresh/" + roomLink);
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/questions/refresh/" + roomLink)
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(CommuncationResponses.getQuestionsBodyResponse(
                                                questions, roomLink, roomName, userId)));
    }

    /**Test if HomeSceneCommunication.getQuestions() return the questions
     */
    @Test
    void shouldGetQuestions() {
        String roomLink = UUID.randomUUID().toString();
        String roomName = "roomName456";
        String userId = UUID.randomUUID().toString();

        Session.getInstance(roomLink, roomName, userId, true);

        Question question1 = new Question("questionText1");
        Question question2 = new Question("questionText2");
        List<Question> questions = new ArrayList<Question>();
        questions.add(question1);
        questions.add(question2);
        mockGetQuestions(questions, roomLink, roomName, userId);

        List<Question> retrievedQuestions = HomeSceneCommunication.getQuestions();

        assertEquals(retrievedQuestions.get(0).getQuestionText(), question1.getQuestionText());
        assertEquals(retrievedQuestions.get(1).getQuestionText(), question2.getQuestionText());
        assertEquals(retrievedQuestions.get(0).getRoom().getLinkIdModerator().toString(), roomLink);
    }

    /**Method to set the mock server for the proper request/response.
     */
    void mockGetSingleQuestion(String roomLink, String userId, Long questionId) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/questions/getOneQuestion/" + roomLink + '/' + userId)
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(questionId.toString()));
    }

    /**Test if HomeSceneCommunication.getSingleQuestion() return the question from the user
     */
    @Test
    void shouldGetSingleQuestions() {
        String roomLink = UUID.randomUUID().toString();
        String roomName = "roomName456";
        String userId = UUID.randomUUID().toString();

        Session.getInstance(roomLink, roomName, userId, true);

        Long questionId = 1L;
        mockGetSingleQuestion(roomLink, userId, questionId);

        Long receivedLong = HomeSceneCommunication.getSingleQuestion();

        assertEquals(questionId, receivedLong);
    }

    /**Test if HomeSceneCommunication.clearQuestions sends
     *  a delete questions request to the right path.
     */
    @Test
    void shouldSendClearQuestionsRequest() {
        String roomLink = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        HomeSceneCommunication.clearQuestions(roomLink);

        String path = "/questions/clearAllQuestions/" + roomLink;
        mockServer.verify(request()
                .withMethod("DELETE")
                .withPath(path));
    }

}
