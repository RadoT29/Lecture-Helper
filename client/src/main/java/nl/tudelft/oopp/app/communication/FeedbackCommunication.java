package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.oopp.app.models.Feedback;
import nl.tudelft.oopp.app.models.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class FeedbackCommunication {

    private static final Gson gson = new Gson();
    private static Session session;

    private static final HttpClient client = HttpClient.newBuilder().build();
    private static HttpResponse<String> response;

    /**
     * Sends POST request with feedback given by a user.
     * @param feedback Feedback feedback given by the user
     */
    public static void sendFeedback(Feedback feedback) {

        String requestBody = gson.toJson(feedback);
        session = Session.getInstance();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/feedback/" + session.getRoomLink()))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("Content-Type", "application/json")
                .build();
        System.out.println("Sending request: " + request.toString());

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Status: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * sends a GET request to the server to get the feedback for the current room.
     * @return list of Feedback or an empty list if the communication with the server fails
     */
    public static List<Feedback> getFeedback() {

        Session session = Session.getInstance();
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/feedback/view/" + session.getRoomLink()))
                .build();

        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Status: " + response.statusCode());
                //Used for testing
                return List.of();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
        return gson.fromJson(response.body(), new TypeToken<List<Feedback>>(){}.getType());
    }
}
