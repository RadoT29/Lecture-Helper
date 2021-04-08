package nl.tudelft.oopp.app.communication;

import nl.tudelft.oopp.app.models.AdminRoom;
import nl.tudelft.oopp.app.models.AdminSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import java.util.List;
import java.util.UUID;

import static nl.tudelft.oopp.app.communication.AdminCommunication.getAdminRooms;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class AdminCommunicationTest {

    private ClientAndServer mockServer;
    private AdminSession session;

    @BeforeEach
    public void setup() {
        mockServer = startClientAndServer(8080);
        session = new AdminSession("password");
    }

    @AfterEach
    public void stopServer() {
        mockServer.stop();
    }

    @Test
    void shouldCheckPasswordTrue() {
        mockCheckPassword("password", true);

        boolean access = AdminCommunication.checkPassword("password");
        assertTrue(access);

    }

    @Test
    void shouldCheckPasswordFalse() {
        mockCheckPassword("password", false);

        boolean access = AdminCommunication.checkPassword("password");
        assertFalse(access);

    }


    void mockCheckPassword(String password, boolean access) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/admin/password/" + password)
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(String.valueOf(access)));


    }


    @Test
    void shouldUnbanAllUsers() {
        String roomLink = UUID.randomUUID().toString();
        String path = "/admin/rooms/unbanUsers/" + session.getPassword()
                + "/" + roomLink;
        session = AdminSession.getInstance("password");
        AdminCommunication.unbanAllUsersForRoom(roomLink);
        mockServer.verify(request()
                .withMethod("PUT")
                .withPath(path)
        );


    }

    @Test
    void shouldGetAdminRooms() {
        String path = "/admin/rooms/unbanUsers/" + session.getPassword();
        session = AdminSession.getInstance("password");

        mockGetAdminRooms(path);
        List<AdminRoom> list = AdminCommunication.getAdminRooms();
        assertNull(list);

    }


    void mockGetAdminRooms(String password) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/admin/password/" + password)
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("null"));


    }


}
