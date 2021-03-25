package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.exceptions.AccessDeniedException;
import nl.tudelft.oopp.app.exceptions.UserWarnedException;
import nl.tudelft.oopp.app.models.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BanCommunication {

    private static Gson gson = new Gson();

    private static HttpClient client = HttpClient.newBuilder().build();

    private static Session session = Session.getInstance();


    /**
     * This method makes a request to the server to save the request/user Ip.
     * @param userId - the user id
     * @param roomLink - the room link
     */
    public static void saveStudentIp(String userId, String roomLink) {
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/room/user/saveIP/" + userId + "/" + roomLink)).build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * This method makes request to the server if the user is banned.
     * @param roomLink - the room id is found later. Check if the user has access for that room
     * @throws AccessDeniedException - if the user is banned this exception is thrown
     */
    public static void isIpBanned(String roomLink) throws AccessDeniedException {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/room/user/isBanned/" + roomLink)).build();
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
        if (result) {
            throw new AccessDeniedException();
        }
        System.out.println("access granted!");
    }

    /**
     * By given question id and room link this request bans users by IP address
     * On the server side by the question id is found the user id,
     * and that user is banned for that specific room.
     * @param questionId - the id of the question
     * @param roomLink - the moderator link of the room. With that is found the room id
     */
    public static void banUserForThatRoom(String questionId, String roomLink) {
        HttpRequest request = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/room/user/banUserRoom/" + questionId + "/" + roomLink)).build();
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
     * This method makes request to the server if the user is banned.
     * @param roomLink - the room id is found later. Check if the user has access for that room
     * @throws AccessDeniedException - if the user is banned this exception is thrown
     */
    public static void isIpWarned(String roomLink) throws UserWarnedException {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/room/user/isWarned/" + roomLink)).build();
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
        if (result) {
            session.setWarned(true);
            throw new UserWarnedException();
        }


    }

    /**
     * By given question id and room link this request bans users by IP address
     * On the server side by the question id is found the user id,
     * and that user is banned for that specific room.
     * @param questionId - the id of the question
     * @param roomLink - the moderator link of the room. With that is found the room id
     */
    public static void warnUserForThatRoom(String questionId, String roomLink) {
        HttpRequest request = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/room/user/warnUserRoom/" + questionId + "/" + roomLink)).build();
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
