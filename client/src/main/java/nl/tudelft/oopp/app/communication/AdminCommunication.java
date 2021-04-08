package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.oopp.app.models.AdminRoom;
import nl.tudelft.oopp.app.models.AdminSession;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class AdminCommunication {

    private static final Gson gson = new Gson();
    private static AdminSession session;

    private static final HttpClient client = HttpClient.newBuilder().build();
    private static HttpResponse<String> response;

    /**
     * Sends a get request to check that the given admin password is correct.
     * @param password the password that needs to be checked
     * @return True if the password is correct, false otherwise
     */
    public static boolean checkPassword(String password) {

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/admin/password/" + password)).build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
            return false;
        }
        return gson.fromJson(response.body(),Boolean.class);
    }

    /**
     * Sends a get request for all the rooms saved on the database.
     * @return a list of the Rooms saved on database
     */
    public static List<AdminRoom> getAdminRooms() {

        session = AdminSession.getInstance();
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/admin/rooms/" + session.getPassword()))
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        return gson.fromJson(response.body(),
                new TypeToken<List<AdminRoom>>() {}.getType());
    }

    /**
     * Sends a put request to unban all students in the given Room.
     * @param roomLink moderator link to identify the room where the users should be unbanned
     */
    public static void unbanAllUsersForRoom(String roomLink) {

        session = AdminSession.getInstance();
        HttpRequest request = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/admin/rooms/unbanUsers/" + session.getPassword()
                + "/" + roomLink))
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


}
