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
     *
     * @param userId   - the user id
     * @param roomLink - the room link
     */
    public static void saveStudentIp(String userId, String roomLink) {
        postRequestResponse("http://localhost:8080/room/user/saveIP/" + userId + "/" + roomLink);
    }


    /**
     * This method makes request to the server if the user is banned.
     *
     * @param roomLink - the room id is found later. Check if the user has access for that room
     * @throws AccessDeniedException - if the user is banned this exception is thrown
     */
    public static void isIpBanned(String roomLink) throws AccessDeniedException {
        boolean result = getRequestResponse("http://localhost:8080/room/user/isBanned/" + roomLink);
        if (result) {
            throw new AccessDeniedException("This user ip is banned for that room");
        }
        System.out.println("access granted!");
    }

    /**
     * By given question id and room link this request bans users by IP address
     * On the server side by the question id is found the user id,
     * and that user is banned for that specific room.
     *
     * @param questionId - the id of the question
     * @param roomLink   - the moderator link of the room. With that is found the room id
     */
    public static void banUserForThatRoom(String questionId, String roomLink) {
        putRequestResponse("http://localhost:8080/room/user/banUserRoom/" + questionId + "/" + roomLink);
    }

    /**
     * This method makes request to the server if the user is banned.
     *
     * @param roomLink - the room id is found later. Check if the user has access for that room
     * @throws AccessDeniedException - if the user is banned this exception is thrown
     */
    public static void isIpWarned(String roomLink) throws UserWarnedException {
        boolean result = getRequestResponse("http://localhost:8080/room/user/isWarned/" + roomLink);
        if (result) {
            session.setWarned(true);
            throw new UserWarnedException("User is warned for misbehaving");
        }
    }

    /**
     * By given question id and room link this request bans users by IP address
     * On the server side by the question id is found the user id,
     * and that user is banned for that specific room.
     *
     * @param questionId - the id of the question
     * @param roomLink   - the moderator link of the room. With that is found the room id
     */
    public static void warnUserForThatRoom(String questionId, String roomLink) {

        putRequestResponse("http://localhost:8080/room/user/warnUserRoom/" + questionId + "/" + roomLink);
    }

    /**
     * This method creates post requests to the server.
     *
     * @param httpRequest - the request string
     */
    public static void postRequestResponse(String httpRequest) {
        HttpRequest request = HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(httpRequest)).build();
        HttpResponse<String> response = tryCatchResponse(request);
    }

    /**
     * This method creates get requests to the server.
     *
     * @param httpRequest - the request string
     */
    public static boolean getRequestResponse(String httpRequest) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create(httpRequest)).build();
        HttpResponse<String> response = tryCatchResponse(request);
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        boolean result = gson.fromJson(response.body(), Boolean.class);
        return result;
    }

    /**
     * This method creates put requests to the server.
     *
     * @param httpRequest - the request string
     */
    public static void putRequestResponse(String httpRequest) {
        HttpRequest request = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create(httpRequest)).build();
        HttpResponse<String> response = tryCatchResponse(request);
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }

    /**
     * This method catch an exception in the response.
     *
     * @param request - httpRequest
     * @return - the response of the server
     */
    public static HttpResponse<String> tryCatchResponse(HttpRequest request) {
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
