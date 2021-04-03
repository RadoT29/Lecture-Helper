package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.exceptions.NoSuchRoomException;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Session;
import nl.tudelft.oopp.app.models.User;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

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
        System.out.println("Client1: " + gson.fromJson(response.body(), Room.class).isOpen());
        return room;
    }

    /**
     * Creates a new room on server and retrieves room info.
     *
     * @param name Name for the room
     * @return Room object with name, links and isOpen boolena
     */
    public static Room scheduleRoom(String name, LocalDateTime startDateUtc) {
        String requestBody = gson.toJson(startDateUtc.toString());
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("Content-Type", "text/plain")
                .uri(URI.create("http://localhost:8080/scheduleRoom?name=" + name.replace(" ", "%20"))).build();
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
        System.out.println("Client1: " + gson.fromJson(response.body(), Room.class).isOpen());
        return room;
    }

    /**
     * Checks whether the user is a Student or a Moderator
     * and creates a session based on this credentials.
     *
     * @throws NoSuchRoomException - throws this exception if the room link is wrong
     */
    public static void checkForRoom(String roomLink) throws NoSuchRoomException {
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
        try {
            // Parses Json response from server.
            User user = gson.fromJson(response.body(), User.class);

            Session.clearSession();
            // Uses the information received to update the session information.
            System.out.println("Is moderator1 " + user.getIsModerator());
            session = session.getInstance(roomLink, String.valueOf(user.id), user.isModerator);
            System.out.println("Is moderator2 " + session.getIsModerator());
            //If the link is not valid then no session is started
            // and user should stay on splash screen.
            if (session == null) {
                System.out.println("Insert valid link");
                throw new NoSuchRoomException("Room with that link does not exist");
            }
        } catch (Exception e) {
            throw new NoSuchRoomException("Room with that link does not exist");
        }

    }

}
