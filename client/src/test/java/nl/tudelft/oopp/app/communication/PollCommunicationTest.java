package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

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


    /**
     * Set up of environment.
     */
    @BeforeEach
    public void setup() {
        mockServer = startClientAndServer(8080);
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

    @AfterEach
    public void stopServer() {
        Session.clearSessionTest();
        mockServer.stop();
    }

    @Test
    void shouldGetPollList() {
        path = "/polls/" + session.getRoomLink();
        mockGetPollList(path);
        List<Poll> polls = PollCommunication.getPolls();
        assertEquals(1, polls.size());
    }

    void mockGetPollList(String path) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath(path)
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(CommuncationResponses
                                        .getPollsList()));


    }

    @Test
    void shouldConstantlyGetPollList() throws InterruptedException {
        path = "/polls/constant/" + session.getRoomLink();
        mockGetPollList(path);
        List<Poll> polls = PollCommunication.constantlyGetPolls(
                String.valueOf(room.getLinkIdModerator()));
        assertEquals(1, polls.size());
    }

    @Test
    void shouldCreatePoll() {
        path = "/polls/" + session.getRoomLink();
        mockCreatePoll(path);
        Long id = PollCommunication.createPoll();
        assertEquals(2, id);
    }

    void mockCreatePoll(String path) {
        mockServer.when(
                request()
                        .withMethod("POST")
                        .withPath(path)
        )
                .respond(
                        response()
                                .withStatusCode(200)
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

    }




}
