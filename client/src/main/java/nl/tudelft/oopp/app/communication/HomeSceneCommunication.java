package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


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
     * @param question the question to be saved on the database
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
            if (response.statusCode() != 200) {
                System.out.println("Status: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Gets list of questions from the server specific to this room link.
     * (all of the questions user's IDs and room ID are already set to zero)
     * @return a list questions
     */
    public static List<Question> getQuestions() {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/questions/refresh/" + session.getRoomLink()))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Question>>(){}.getType());
    }

    /**
     * Method to send a DELETE request to the server
     * so to delete all of the questions created so far in a particular room.
     * @param roomLink - Link of the room from which the request was made from
     */
    public static void clearQuestions(String roomLink) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/questions/clearAllQuestions/" + roomLink))
                .DELETE()
                .build();
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



}
