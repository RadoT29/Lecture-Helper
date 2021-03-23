package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class QuestionCommunicationTest {
    private ClientAndServer mockServer;
    private Gson gson = new Gson();

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
    void shouldSendDismissQuestionRequest() {
        long questionId = 1L;

        QuestionCommunication.dismissQuestion(questionId);

        String path = "/questions/dismiss/" + questionId;
        mockServer.verify(request()
                .withMethod("DELETE")
                .withPath(path));
    }

    @Test
    void shouldSendDismissQuestionRequestFromStudent() {
        long questionId = 1L;
        long userId = 1L;

        QuestionCommunication.dismissSingular(questionId, userId);

        String path = "/questions/dismissSingular/" + questionId + '/' + userId;
        mockServer.verify(request()
                .withMethod("DELETE")
                .withPath(path));
    }

    @Test
    void shouldSendUpVoteRequest() {
        String roomLink = UUID.randomUUID().toString();
        String roomName = "roomName456";
        String userId = UUID.randomUUID().toString();

        Session.getInstance(roomLink, roomName, userId, true);

        String questionId = "1";

        QuestionCommunication.upVoteQuestion(questionId);

        String path = "/questions/changeUpvote/" + questionId + '/' + userId;
        mockServer.verify(request()
                .withMethod("POST")
                .withPath(path));
    }

    @Test
    void shouldSendDeleteUpVoteRequest() {
        String roomLink = UUID.randomUUID().toString();
        String roomName = "roomName456";
        String userId = "1";

        Session.getInstance(roomLink, roomName, userId, true);

        String questionId = "1L";

        QuestionCommunication.deleteUpvote(questionId);

        String path = "/questions/deleteUpvote/" + questionId + '/' + userId;
        mockServer.verify(request()
                .withMethod("DELETE")
                .withPath(path));
    }

    @Test
    void shouldSendEditQuestionRequest() {
        String questionId = "1L";
        String questionText = "questiooon";

        QuestionCommunication.editQuestionText(questionId, questionText);

        String path = "/questions/edit/" + questionId;
        mockServer.verify(request()
                .withMethod("POST")
                .withPath(path)
                .withBody(gson.toJson(questionText)));
    }
}
