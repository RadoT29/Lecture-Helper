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

public class FeedBackCommunicationTest {
    private ClientAndServer mockServer;
    private Session session;
    private Feedback feedback;
    private long userId;
    private Room room;

    /**
     * Set up of environment.
     */
    @BeforeEach
    public void setup() {
        mockServer = startClientAndServer(8080);
        feedback = new Feedback("Alright", 4);
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
    void shouldSendFeedBack() {
        String path = "/feedback/" + session.getRoomLink();
        FeedbackCommunication.sendFeedback(feedback);
        mockServer.verify(request()
                    .withMethod("POST")
                    .withPath(path));

    }


    @Test
    void shouldGetFeedBack() {
        String path = "/feedback/view/" + session.getRoomLink();

        mockGetFeedBack(path);
        List<Feedback> feedbackList = FeedbackCommunication.getFeedback();
        assertEquals(1, feedbackList.size());

    }

    void mockGetFeedBack(String path) {
        mockServer.when(
                    request()
                            .withMethod("GET")
                            .withPath(path)
            )
                    .respond(
                            response()
                                    .withStatusCode(200)
                                    .withBody(CommuncationResponses
                                            .getFeedBackList(room.getName())));


    }
}
