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

    @BeforeEach
    public void startMockServer() {
        mockServer = startClientAndServer(8080);
    }

    @AfterEach
    public void stopMockServer() {
        Session.clearSession();
        mockServer.stop();
    }

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
