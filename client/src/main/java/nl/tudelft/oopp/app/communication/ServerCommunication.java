package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.models.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerCommunication {

    private static final Gson gson = new Gson();
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static HttpResponse<String> response;
    private static Session session;

    /**
     * Close the room.
     *
     * @param linkId - name of the room
     */
    public static void closeRoomStudents(String linkId) {

        HttpRequest request = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/closeRoomForStudents/" + linkId)).build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

    }

    /**
     * .
     * Make get requested if the room is open
     *
     * @param linkId - link of the room
     * @return boolean true if the room is open, otherwise false
     * @throws NoStudentPermissionException - throws the exception when is
     *      tried to entry in closed room
     */
    public static boolean isRoomOpenStudents(String linkId) throws NoStudentPermissionException {

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/hasStudentPermission/" + linkId)).build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        boolean result = gson.fromJson(response.body(), Boolean.class);

        if (result) {
            return result;
        } else {
            throw new NoStudentPermissionException();
        }

    }

    /**
     * Sends a request to server to change user's name.
     *
     * @param userId   - the id of the user.
     * @param nickName - the name of the user.
     */
    public static void setNick(String userId, String nickName) {

        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/room/user/" + userId + "/nick/"
                        + nickName))
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * This method sets the name of the room for this session.
     */
    public static void getRoomName() {

        session = session.getInstance();

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/room/name/" + session.getRoomLink())).build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            return;
        }

        session = session.getInstance();
        session.setRoomName(response.body());

    }

    /**
     * This method makes put request to the server to open a room by provided room link.
     *  @param linkId - the room link
     */
    public static void openRoomStudents(String linkId) {

        HttpRequest request = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/openRoomForStudents/" + linkId)).build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * Gets the studentLink associated with the room with the given Moderator link.
     * @param moderatorLink The moderatorLink to identify the room you need.
     * @return The studentLink associated with your moderatorLink.
     */
    public static String getStudentLink(String moderatorLink) {

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/room/getStudentLink/" + moderatorLink)).build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        return response.body();
    }
}
