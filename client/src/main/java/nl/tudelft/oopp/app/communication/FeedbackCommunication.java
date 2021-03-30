package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.models.Feedback;
import nl.tudelft.oopp.app.models.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class FeedbackCommunication {

    private static Gson gson = new Gson();
    private static Session session;

    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * Sends POST request with feedback given by a user.
     * @param roomLink String roomLink of a room to add comment to
     * @param feedback Feedback feedback given by the user
     */
    public static void sendFeedback(String roomLink, Feedback feedback) {
        String requestBody = gson.toJson(feedback);
        session = Session.getInstance();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/feedback/" + session.getRoomLink()))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("Content-Type", "application/json")
                .build();
        System.out.println("Sending request: " + request.toString());

        HttpResponse<String> response = null;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Status: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static List<Feedback> getFeedback() {
        List<Feedback> list = new ArrayList<>();
        list.add(new Feedback("This is a very nice comment about this room.", 5));
        list.add(new Feedback("This is a very long comment about the room, helloAn administrator can see the history of activities" +
                "Persistent database" +
                "Students can give feedback" +
                "Moderators see the statistics of the feedback" +
                "The name of the user who asked a question is displayed" +
                "Students can only send a limited amount of questions " +
                "Moderators can ban users by their IP ", 3));
        return list;
    }
}
