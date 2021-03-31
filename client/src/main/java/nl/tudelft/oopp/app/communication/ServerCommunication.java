package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.models.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerCommunication {

    private static Gson gson = new Gson();

    private static HttpClient client = HttpClient.newBuilder().build();

    private static Session session;

    /**
     * Close the room.
     *
     * @param linkId - name of the room
     * @throws Exception if communication with the server fails.
     */
    public static void closeRoomStudents(String linkId) {
        HttpRequest request = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/closeRoomForStudents/" + linkId)).build();
        HttpResponse<String> response = null;
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
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        System.out.println(response.body());
        boolean result = gson.fromJson(response.body(), Boolean.class);
        System.out.println("The room is open:" + result);
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
                .uri(URI.create("http://localhost:8080/room/user/" + userId.replace(" ", "%20") + "/nick/" + nickName.replace(" ", "%20"))).build();
        HttpResponse<String> response = null;
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
        HttpResponse<String> response = null;

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
        HttpResponse<String> response = null;
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
}
