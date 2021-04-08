package nl.tudelft.oopp.app.communication;

import nl.tudelft.oopp.app.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class ReactionCommunicationTest {

    private ClientAndServer mockServer;
    private Session session;
    private Reaction emotionReaction;
    private Reaction speedReaction;
    private long userId;
    private Room room;

    /**
     * Set up of environment.
     */
    @BeforeEach
    public void setup() {
        mockServer = startClientAndServer(8080);
        emotionReaction = new EmotionReaction(1);
        speedReaction = new SpeedReaction(1);
        userId = 1L;

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
    void shouldPostReactionTypeEmotion() {

        String path = "/reactions/emotion/"
                + session.getRoomLink() + "/" + session.getUserId();
        ReactionCommunication.postReaction(emotionReaction);
        mockServer.verify(request()
                .withMethod("POST")
                .withPath(path));

    }

    @Test
    void shouldPostReactionTypeSpeed() {

        String path = "/reactions/speed/"
                + session.getRoomLink() + "/" + session.getUserId();
        ReactionCommunication.postReaction(speedReaction);
        mockServer.verify(request()
                .withMethod("POST")
                .withPath(path));

    }


    @Test
    void shouldGetReactionStatisticsEmotion() {

        String path = "/reactions/stat/emotion/"
                + session.getRoomLink();
        mockReactionStats(path);
        Integer reaction = ReactionCommunication.getReactionStats(false);
        assertEquals(1, reaction);
    }

    @Test
    void shouldGetReactionStatisticsSpeed() {

        String path = "/reactions/stat/speed/"
                + session.getRoomLink();
        mockReactionStats(path);
        Integer reaction = ReactionCommunication.getReactionStats(true);
        assertEquals(1, reaction);
    }

    void mockReactionStats(String path) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath(path)
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(String.valueOf(1)));

    }

    @Test
    void shouldGetAllReactionCount() {
        String path = "/reactions/counts/emotion/"
                + session.getRoomLink();
        String response = "[1,3,1]";
        int statusCode = 200;
        mockGetAllReactions(path, response, statusCode);

        List<Integer> reactionCount = ReactionCommunication.getAllReactionCount();
        assertEquals(3, reactionCount.get(1));
    }

    /**
     * When the list returned by server does not correspond to expected
     * the method should set [0,0,0] as the list.
     */
    @Test
    void shouldGetAllReactionCountError() {
        String path = "/reactions/counts/emotion/"
                + session.getRoomLink();
        String response = "[1,3,1,4]";
        int statusCode = 200;
        mockGetAllReactions(path, response, statusCode);
        List<Integer> reactionCount = ReactionCommunication.getAllReactionCount();
        assertEquals(0, reactionCount.get(1));
    }


    /**
     * When status code is not 200 return [0,0,0].
     */
    @Test
    void shouldGetAllReactionCountError2() {
        String path = "/reactions/counts/emotion/"
                + session.getRoomLink();
        String response = "[1,3,1]";
        int statusCode = 300;
        mockGetAllReactions(path, response, statusCode);
        List<Integer> reactionCount = ReactionCommunication.getAllReactionCount();
        assertEquals(0, reactionCount.get(1));
    }

    void mockGetAllReactions(String path, String response, int statusCode) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath(path)
        )
                .respond(
                        response()
                                .withStatusCode(statusCode)
                                .withBody(response));

    }


}
