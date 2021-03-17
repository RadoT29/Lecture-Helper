package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.oopp.app.exceptions.NoStudentPermissionException;
import nl.tudelft.oopp.app.exceptions.RoomIsClosedException;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
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

    /**.
     * Make get requested if the room is open
     *
     * @param linkId - link of the room
     * @return boolean true if the room is open, otherwise false
     */
    public static boolean isTheRoomClosed(String linkId) throws RoomIsClosedException {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/isOpenById/" + linkId)).build();
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
        if(result==false)throw new RoomIsClosedException();
        return result;
    }

    /**.
     * Make a requested if the students has permission to the room
     *
     * @param linkId - link of the room
     * @return boolean true if the room is open, otherwise false
     */
    public static boolean hasStudentPermission(String linkId) throws NoStudentPermissionException {
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
        if(result==false)throw new NoStudentPermissionException();
        return result;
    }

    /**
     * Kick all students.
     *
     * @param linkId - link of the room
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
}
