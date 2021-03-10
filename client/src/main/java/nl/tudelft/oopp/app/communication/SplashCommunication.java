package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SplashCommunication {

    private static Gson gson = new Gson();

    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * Creates a new room on server and retrieves room info.
     * @param name Name for the room
     * @return Room object with name, links and isOpen boolena
     */
    public static Room postRoom(String name) {
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/room?name=" + name.replace(" ","%20"))).build();
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
        Session session = Session.getInstance(room.linkIdModerator.toString(), room.name, true);
        return room;
    }

}
