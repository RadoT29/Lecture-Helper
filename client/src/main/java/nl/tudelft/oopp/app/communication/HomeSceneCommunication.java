package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


/**
 * This class contains the interactions between the home page on the client and the server.
 */
public class HomeSceneCommunication {

    private static Gson gson = new Gson();
    private static Session session = Session.getInstance();

    private static HttpClient client = HttpClient.newBuilder().build();

    /**
     * This method makes a POST Request on the server with the question object as body
     * and the roomLink and userId as path variables
     * so that the question is associated with the right room and user.
     * @param question the question to be saved on the databse
     */

    public static void postQuestion(Question question) {
        String requestBody = gson.toJson(question);

        //Creates a new POST Request at the link question/roomLink/userId
        // with the Question object as body
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/questions/" + session.getRoomLink() + "/" + session.getUserId()))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("Content-Type", "application/json")
                .build();
        System.out.println("Sending request: " + request.toString());

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

}

