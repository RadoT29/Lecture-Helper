package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.exceptions.NoSuchRoomException;
import nl.tudelft.oopp.app.models.Moderator;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Session;
import nl.tudelft.oopp.app.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Body;

import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import static org.junit.jupiter.api.Assertions.*;

public class SplashCommunicationTest {

    private ClientAndServer mockServer;
    private Session session;
    private String roomName;
    private static final Gson gson = new Gson();


    /**Start the mock server before each test.
     */
    @BeforeEach
    public void startMockServer() {
        mockServer = startClientAndServer(8080);
        roomName = "newRoom";
        session = new Session();
    }

    /**Close the mock server after each test so they are completely isolated.
     * Clear the sessions singleton for the same reason.
     */
    @AfterEach
    public void stopMockServer() {
        Session.clearSessionTest();
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
    void shouldPostRoom() {

        mockPostRoom(roomName);
        Room room = SplashCommunication.postRoom(roomName);
        System.out.println(room);
        assertEquals(room.getName(), roomName);
    }

    /**Test if SplashCommunication.postRoom return a created room with the room links
     */
    @Test
    void shouldReceiveIdLinks() {

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

    /**
     * Test if SplashCommunication.postRoom return a user and it is saved in the Session singleton
     */
    @Test
    void shouldCreateUserAndSaveSession() throws NoSuchRoomException {

        mockPostRoom(roomName);
        Room room = SplashCommunication.postRoom(roomName);
        String roomLink = room.getLinkIdModerator().toString();

        mockCheckRoom(roomLink, roomName);
        SplashCommunication.checkForRoom(roomLink);

        //Gets the session with the updated information
        session = Session.getInstance();

        assertEquals(session.getRoomLink(), roomLink);
    }

    /**Test if SplashCommunication.postRoom creates Session with invalid links
     */
    @Test
    void shouldNotCreateUserWithInvalidLink() throws NoSuchRoomException {

        mockPostRoom(roomName);
        Room room = SplashCommunication.postRoom(roomName);
        String roomLink = room.getLinkIdModerator().toString();
        mockCheckRoom(roomLink, roomName);
        SplashCommunication.checkForRoom("roomLink");
        //Gets the session with the updated information
        Session session = Session.getInstance();

        assertNull(session);
    }
    /**
    Test
    void shouldScheduleRoom() {
    LocalDateTime timeSet = LocalDateTime.now();
    timeSet.plusMinutes(10);
    String requestBody = gson.toJson(timeSet.toString());

    mockScheduleRoom(requestBody, "false");
    Room room = SplashCommunication.scheduleRoom(roomName, timeSet);

    assertFalse(room.isPermission());
    }

    void mockScheduleRoom(String timeSet, String isOpen) {
    mockServer.when(
    request()
    .withMethod("POST")
    .withPath("/scheduleRoom?name=" + roomName.replace(" ", "%20")))
    .respond(
    response()
    .withStatusCode(200)
    .withBody(CommuncationResponses.postRoomBodyResponse1(roomName)));

    }.


    */
    public void something() {

    }
}
