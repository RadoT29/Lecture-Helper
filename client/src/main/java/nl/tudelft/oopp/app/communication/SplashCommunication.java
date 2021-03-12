package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Session;
import nl.tudelft.oopp.app.models.User;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SplashCommunication {

    private static Gson gson = new Gson();

    private static HttpClient client = HttpClient.newBuilder().build();

    private static Session session;

    /**
     * Creates a new room on server and retrieves room info.
     *
     * @param name Name for the room
     * @return Room object with name, links and isOpen boolena
     */
    public static Room postRoom(String name) {
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/room?name=" + name.replace(" ", "%20"))).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        Room room = gson.fromJson(response.body(), Room.class);
        return room;
    }

    /**
     * Checks whether the user is a Student or a Moderator
     * and creates a session based on this credentials.
     */
    public static void checkForRoom(String roomLink) {
        System.out.println("This worked - checkForRoom !!!");
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create("http://localhost:8080/room/user/" + roomLink)).build();
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
        // Parses Json response from server
        User user = gson.fromJson(response.body(), User.class);
        // Uses the information received to update the session information
        session = session.getInstance(roomLink, String.valueOf(user.id), user.isModerator);

    }


}
