package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.UUID;

import java.net.http.HttpRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class PollCommunicationTest {

    private static final Gson gson = new Gson();
    private ClientAndServer mockServer;
    private Session session;
    private Feedback feedback;
    private long userId;
    private Room room;
    private Question question;
    private String path;
    private PrintStream standardOut = System.out;
    private ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();


    /**
     * Set up of environment.
     */
    @BeforeEach
    public void setup() {
        mockServer = startClientAndServer(8080);
        System.setOut(new PrintStream(outputStreamCaptor));

        feedback = new Feedback("Alright", 4);
        userId = 1L;

        question = new Question("text");
        question.setId(1L);

        room = new Room("roomName456");
        UUID roomLink = UUID.randomUUID();
        room.setLinkIdModerator(roomLink);

        session = Session.getInstance(room.getLinkIdModerator().toString(),
                room.getName(), String.valueOf(userId), true);
    }

    /**
     * Stops server.
     */
    @AfterEach
    public void stopServer() {
        System.setOut(standardOut);
        Session.clearSessionTest();
        mockServer.stop();
    }

    @Test
    void shouldGetPollList() {
        path = "/polls/" + session.getRoomLink();
        mockGetPollList(path, 200);
        List<Poll> polls = PollCommunication.getPolls();
        assertEquals(1, polls.size());
    }

    @Test
    void shouldGetPollListError() {
        path = "/polls/" + session.getRoomLink();
        mockGetPollList(path, 300);
        PollCommunication.getPolls();
        assertTrue(outputStreamCaptor.toString()
                .trim().contains("Status: 300"));
    }

    void mockGetPollList(String path, int statusCode) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath(path)
        )
                .respond(
                        response()
                                .withStatusCode(statusCode)
                                .withBody(CommuncationResponses
                                        .getPollsList()));


    }

    @Test
    void shouldConstantlyGetPollList() throws InterruptedException {
        path = "/polls/constant/" + session.getRoomLink();
        mockGetPollList(path, 200);
        List<Poll> polls = PollCommunication.constantlyGetPolls(
                String.valueOf(room.getLinkIdModerator()));
        assertEquals(1, polls.size());
    }

    @Test
    void shouldConstantlyGetPollListError() throws InterruptedException {
        path = "/polls/constant/" + session.getRoomLink();
        mockGetPollList(path, 300);
        List<Poll> polls = PollCommunication.constantlyGetPolls(
                String.valueOf(room.getLinkIdModerator()));
        assertTrue(outputStreamCaptor.toString()
                .trim().contains("Status: 300"));
    }


    @Test
    void shouldCreatePoll() {
        path = "/polls/" + session.getRoomLink();
        mockCreatePoll(path, 200);
        Long id = PollCommunication.createPoll();
        assertEquals(2, id);
    }


    @Test
    void shouldCreatePollError() {
        path = "/polls/" + session.getRoomLink();
        mockCreatePoll(path, 300);
        PollCommunication.createPoll();
        assertTrue(outputStreamCaptor.toString()
                .trim().contains("Status: 300"));
    }

    void mockCreatePoll(String path, int statusCode) {
        mockServer.when(
                request()
                        .withMethod("POST")
                        .withPath(path)
        )
                .respond(
                        response()
                                .withStatusCode(statusCode)
                                .withBody("2"));


    }

    @Test
    void shouldUpdatePoll() {
        path = "/polls/" + session.getRoomLink() + '/' + 1;
        Poll poll = new Poll();
        PollCommunication.updatePoll(1, poll);

        mockServer.verify(request()
                .withMethod("PUT")
                .withHeader("Content-Type", "application/json")
                .withPath(path)
        );
    }

    @Test
    void shouldFinishPoll() {
        path = "/polls/" + session.getRoomLink() + '/' + 1 + "/finish";
        PollCommunication.finishPoll(1);

        mockServer.verify(request()
                .withMethod("PUT")
                .withPath(path)
        );
    }

    @Test
    void shouldGetPollAnswers() {
        path = "/polls/answer/" + session.getUserId() + '/' + 1;
        mockGetPollAnswers(path, 200);
        List<PollAnswer> list = PollCommunication.getAnswers(1);
        assertEquals(0, list.size());
    }

    @Test
    void shouldGetPollAnswersError() {
        path = "/polls/answer/" + session.getUserId() + '/' + 1;
        mockGetPollAnswers(path, 300);
        PollCommunication.getAnswers(1);
        assertTrue(outputStreamCaptor.toString()
                .trim().contains("Status: 300"));
    }




    void mockGetPollAnswers(String path, int statusCode) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath(path)
        )
                .respond(
                        response()
                                .withStatusCode(statusCode)
                                .withBody("[]"));


    }


    @Test
    void shouldSendAnswer() {
        path = "/polls/answer" + '/' + session.getUserId();
        PollCommunication.sendAnswers(new Poll());

        mockServer.verify(request()
                .withMethod("POST")
                .withPath(path)
        );
    }

}
