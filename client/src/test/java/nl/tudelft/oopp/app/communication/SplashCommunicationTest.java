package nl.tudelft.oopp.app.communication;

import nl.tudelft.oopp.app.exceptions.NoSuchRoomException;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import static org.junit.jupiter.api.Assertions.*;

public class SplashCommunicationTest {
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

    void mockPostRoom(String roomName){
        mockServer.when(
                request()
                        .withMethod("POST")
                        .withPath("/room")
                        .withQueryStringParameter("name", roomName)
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("{\n  \"id\": 2,\n  \"name\": \"" + roomName + "\",\n  " +
                                        "\"linkIdStudent\": \"f71b9850-a0b1-4ec6-b85e-ba4fffeaa7c9\",\n  " +
                                        "\"linkIdModerator\": \"bea8d9bc-a3fd-4070-9dae-d4ceadfad3a6\",\n  " +
                                        "\"isOpen\": true,\n  \"permission\": true,\n  \"createdAt\": " +
                                        "\"2021-03-22T14:58:31.109+0000\",\n  \"updatedAt\": " +
                                        "\"2021-03-22T14:58:31.109+0000\"\n}")
                );
    }

    @Test
    void shouldCreateARoom() {
        String roomName = "new room";
        mockPostRoom(roomName);
        Room room = SplashCommunication.postRoom(roomName);
        assertEquals(room.getName(), roomName);
    }

    @Test
    void shouldReceiveIdLinks() {
        String roomName = "new room";
        mockPostRoom(roomName);
        Room room = SplashCommunication.postRoom(roomName);
        assertNotNull(room.getLinkIdModerator());
        assertNotNull(room.getLinkIdStudent());
    }

    void mockCheckRoom(String roomLink, String roomName){
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/room/user/" + roomLink)
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("{\n  \"id\": 1,\n  \"name\": \"Anonymous\",\n  " +
                                        "\"isModerator\": false,\n  \"roomId\": {\n    \"id\": 1,\n    " +
                                        "\"name\": \"" + roomName + "\",\n    \"linkIdStudent\": " +
                                        "\"" + roomLink + "\",\n    " +
                                        "\"linkIdModerator\": \"" + roomLink + "\"," +
                                        "\n    \"isOpen\": true,\n    \"permission\": true,\n    " +
                                        "\"createdAt\": \"2021-03-22T16:56:05.303+0000\",\n    " +
                                        "\"updatedAt\": \"2021-03-22T16:56:05.303+0000\"\n  },\n  " +
                                        "\"createdAt\": \"2021-03-22T16:56:16.630+0000\",\n  \"updatedAt\": " +
                                        "\"2021-03-22T16:56:16.630+0000\"\n}")
                );
    }

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

    void mockGetQuestions(List<Question> questions, String roomLink, String roomName, String userId) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/questions/refresh/" + roomLink)
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("[{\n    \"id\": 1,\n    \"upVotes\": 0,\n    " +
                                        "\"room\": {\n      \"id\": 0,\n      \"name\": \"" + roomName + "\",\n" +
                                        "      \"linkIdStudent\": \"" + roomLink + "\",\n      " +
                                        "\"linkIdModerator\": \"" + roomLink + "\",\n      " +
                                        "\"isOpen\": true,\n      \"permission\": true,\n      \"createdAt\": " +
                                        "\"2021-03-22T17:38:34.544+0000\",\n      \"updatedAt\": " +
                                        "\"2021-03-22T17:38:34.544+0000\"\n    },\n    \"user\": {\n      " +
                                        "\"id\": " + userId + ",\n      \"name\": \"mod01\",\n      \"isModerator\": true,\n      " +
                                        "\"roomId\": {\n        \"id\": 0,\n        \"name\": \"roomdo\",\n        " +
                                        "\"linkIdStudent\": \"" + roomLink + "\",\n        " +
                                        "\"linkIdModerator\": \"" + roomLink + "\",\n        " +
                                        "\"isOpen\": true,\n        \"permission\": true,\n        \"createdAt\": " +
                                        "\"2021-03-22T17:38:34.544+0000\",\n        \"updatedAt\": " +
                                        "\"2021-03-22T17:38:34.544+0000\"\n      },\n      \"createdAt\": " +
                                        "\"2021-03-22T17:38:41.188+0000\",\n      \"updatedAt\": " +
                                        "\"2021-03-22T17:38:41.188+0000\"\n    },\n    \"questionText\": " +
                                        "\"" + questions.get(0).getQuestionText() + "\",\n    \"createdAt\": \"2021-03-22T18:38:52.1034\",\n    " +
                                        "\"updatedAt\": \"2021-03-22T18:38:52.10343\"\n  }," +
                                        "{\n    \"id\": 1,\n    \"upVotes\": 0,\n    " +
                                        "\"room\": {\n      \"id\": 0,\n      \"name\": \"" + roomName + "\",\n" +
                                        "      \"linkIdStudent\": \"" + roomLink + "\",\n      " +
                                        "\"linkIdModerator\": \"" + roomLink + "\",\n      " +
                                        "\"isOpen\": true,\n      \"permission\": true,\n      \"createdAt\": " +
                                        "\"2021-03-22T17:38:34.544+0000\",\n      \"updatedAt\": " +
                                        "\"2021-03-22T17:38:34.544+0000\"\n    },\n    \"user\": {\n      " +
                                        "\"id\": " + userId + ",\n      \"name\": \"mod01\",\n      \"isModerator\": true,\n      " +
                                        "\"roomId\": {\n        \"id\": 0,\n        \"name\": \"roomdo\",\n        " +
                                        "\"linkIdStudent\": \"" + roomLink + "\",\n        " +
                                        "\"linkIdModerator\": \"" + roomLink + "\",\n        " +
                                        "\"isOpen\": true,\n        \"permission\": true,\n        \"createdAt\": " +
                                        "\"2021-03-22T17:38:34.544+0000\",\n        \"updatedAt\": " +
                                        "\"2021-03-22T17:38:34.544+0000\"\n      },\n      \"createdAt\": " +
                                        "\"2021-03-22T17:38:41.188+0000\",\n      \"updatedAt\": " +
                                        "\"2021-03-22T17:38:41.188+0000\"\n    },\n    \"questionText\": " +
                                        "\"" + questions.get(1).getQuestionText() + "\",\n    \"createdAt\": \"2021-03-22T18:38:52.1034\",\n    " +
                                        "\"updatedAt\": \"2021-03-22T18:38:52.10343\"\n  }]")
                );
    }

    @Test
    void shouldGetQuestions() {
        String roomLink = UUID.randomUUID().toString();
        String roomName = "roomName456";
        String userId = UUID.randomUUID().toString();
        Session session = Session.getInstance(roomLink, roomName, userId, true);
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
}
