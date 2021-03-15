package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

public class ServerCommunication {

    private static Gson gson = new Gson();

    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * Close the room.
     *
     * @param linkId - name of the room
     * @throws Exception if communication with the server fails.
     */
    public static void closeRoom(String linkId) {
        HttpRequest request = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/closeRoomById/" + linkId)).build();
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
     * Kick all students.
     *
     * @param linkId - name of the room
     * @throws Exception if communication with the server fails.
     */
    public static void kickAllStudents(String linkId) {
        HttpRequest request = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/kickAllStudents/" + linkId)).build();
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
     * Sends a request to server to change user's name.
     * @param userId - the id of the user.
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
}
