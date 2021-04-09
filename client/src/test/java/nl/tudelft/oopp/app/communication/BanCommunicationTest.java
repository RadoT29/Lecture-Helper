package nl.tudelft.oopp.app.communication;

import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.OutOfLimitOfQuestionsException;
import nl.tudelft.oopp.app.exceptions.UserWarnedException;
import nl.tudelft.oopp.app.models.Feedback;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class BanCommunicationTest {
    private ClientAndServer mockServer;
    private Session session;
    private Feedback feedback;
    private long userId;
    private Room room;
    private Question question;

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
    void shouldSaveStudentIp() {
        String path = "/room/user/saveIP/" + userId + "/" + room.getLinkIdModerator();
        BanCommunication.saveStudentIp(String.valueOf(userId),
                String.valueOf(room.getLinkIdModerator()));
        mockServer.verify(request()
                    .withMethod("POST")
                    .withPath(path));
    }

    @Test
    void shouldGetIsIpBannedSuccessful() {
        String path = "/room/user/isBanned/" + room.getLinkIdModerator();
        mockGetIsIpBanned(path, false, 200);
        assertDoesNotThrow(() -> {
            BanCommunication.isIpBanned(String.valueOf(room.getLinkIdModerator()));
        });
    }

    @Test
    void shouldGetIsIpBannedException() {
        String path = "/room/user/isBanned/" + room.getLinkIdModerator();
        mockGetIsIpBanned(path, true, 200);
        assertThrows(AccessDeniedException.class, () -> {
            BanCommunication.isIpBanned(String.valueOf(room.getLinkIdModerator()));
        });
    }


    @Test
    void shouldGetIsIpBannedError() {
        String path = "/room/user/isBanned/" + room.getLinkIdModerator();
        mockGetIsIpBanned(path, false, 300);
        assertThrows(AccessDeniedException.class, () -> {
            BanCommunication.isIpBanned(String.valueOf(room.getLinkIdModerator()));
        });
    }




    void mockGetIsIpBanned(String path, boolean access, int statusCode) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath(path)
        )
                .respond(
                        response()
                                .withStatusCode(statusCode)
                                .withBody(String.valueOf(access)));


    }


    @Test
    void shouldBanUserForRoom() {
        String path = "/room/user/banUserRoom/" + question.getId()
                + "/" + room.getLinkIdModerator();
        BanCommunication.banUserForThatRoom(String.valueOf(question.getId()),
                                        String.valueOf(room.getLinkIdModerator()));

        mockServer.verify(request()
                    .withMethod("PUT")
                    .withPath(path)
        );
    }

    @Test
    void shouldGetIsIpWarnedFalse() {
        String path = "/room/user/isWarned/" + room.getLinkIdModerator();
        mockIsIpWarned(path, false);

        assertDoesNotThrow(() -> {
            BanCommunication.isIpWarned(String.valueOf(room.getLinkIdModerator()));
        });
    }

    @Test
    void shouldGetIsIpWarnedTrue() {
        String path = "/room/user/isWarned/" + room.getLinkIdModerator();
        mockIsIpWarned(path, true);

        assertThrows(UserWarnedException.class, () -> {
            BanCommunication.isIpWarned(String.valueOf(room.getLinkIdModerator()));
        });
    }


    void mockIsIpWarned(String path, boolean warned) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath(path)
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(String.valueOf(warned)));
    }


    @Test
    void shouldWarnUser() {
        String path = "/room/user/warnUserRoom/"
                + question.getId() + "/" + room.getLinkIdModerator();
        BanCommunication.warnUserForThatRoom(String.valueOf(question.getId()),
                String.valueOf(room.getLinkIdModerator()));

        mockServer.verify(request()
                .withMethod("PUT")
                .withPath(path)
        );
    }

}
