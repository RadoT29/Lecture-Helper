package nl.tudelft.oopp.app.communication;

import nl.tudelft.oopp.app.exceptions.NoSuchRoomException;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import static org.junit.jupiter.api.Assertions.*;

public class SplashCommunicationTest {
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
        Session.clearSession();
        mockServer.stop();
    }

    /**Method to set the mock server for the proper request/response.
     */
    void mockPostRoom(String roomName) {
        mockServer.when(
                request()
                        .withMethod("POST")
                        .withPath("/room")
                        .withQueryStringParameter("name", roomName)
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(CommuncationResponses.postRoomBodyResponse(roomName)));
    }

    /**Test if SplashCommunication.postRoom return a created room with the name that was given
     */
    @Test
    void shouldCreateARoom() {
        String roomName = "new room";
        mockPostRoom(roomName);
        Room room = SplashCommunication.postRoom(roomName);
        assertEquals(room.getName(), roomName);
    }

    /**Test if SplashCommunication.postRoom return a created room with the room links
     */
    @Test
    void shouldReceiveIdLinks() {
        String roomName = "new room";
        mockPostRoom(roomName);
        Room room = SplashCommunication.postRoom(roomName);
        assertNotNull(room.getLinkIdModerator());
        assertNotNull(room.getLinkIdStudent());
    }

    /**Method to set the mock server for the proper request/response.
     */
    void mockCheckRoom(String roomLink, String roomName) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/room/user/" + roomLink)
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(CommuncationResponses
                                        .checkRoomBodyResponse(roomName, roomLink)));
    }

    /**Test if SplashCommunication.postRoom return a user and it is saved in the Session singleton
     */
    @Test
    void shouldCreateUserAndSaveSession() throws NoSuchRoomException {
        String roomName = "new room";
        mockPostRoom(roomName);
        Room room = SplashCommunication.postRoom(roomName);
        String roomLink = room.getLinkIdModerator().toString();
        mockCheckRoom(roomLink, roomName);
        SplashCommunication.checkForRoom(roomLink);
        //Gets the session with the updated information
        Session session = Session.getInstance();

        assertEquals(session.getRoomLink(), roomLink);
    }

    /**Test if SplashCommunication.postRoom creates Session with invalid links
     */
    @Test
    void shouldNotCreateUserWithInvalidLink() throws NoSuchRoomException {
        String roomName = "new room";
        mockPostRoom(roomName);
        Room room = SplashCommunication.postRoom(roomName);
        String roomLink = room.getLinkIdModerator().toString();
        mockCheckRoom(roomLink, roomName);
        SplashCommunication.checkForRoom("roomLink");
        //Gets the session with the updated information
        Session session = Session.getInstance();

        assertNull(session);
    }
}
