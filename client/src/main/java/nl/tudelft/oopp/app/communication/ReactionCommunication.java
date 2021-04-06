package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.oopp.app.models.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ReactionCommunication {

    private static final Gson gson = new Gson();
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static HttpResponse<String> response;

    private static Session session;


    /**
     * This method makes a POST Request on the server with a Reaction object as body
     * and the roomLink and userId as path variables
     * so that the reaction is associated with the right room and user.
     * @param reaction the question to be saved on the database
     */
    public static void postReaction(Reaction reaction) {

        // Gets path variable for the POST based on the Reaction type
        String typeOfReaction = null;
        if (reaction instanceof SpeedReaction) {
            typeOfReaction = "speed";
        } else if (reaction instanceof EmotionReaction) {
            typeOfReaction = "emotion";
        }

        session = session.getInstance();

        String requestBody = gson.toJson(reaction);

        //Creates a new POST Request at the link reactions/typeOfReaction/roomLink/userId
        // with the Reaction object as body
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/reactions/"
                        + typeOfReaction + "/"
                        + session.getRoomLink() + "/" + session.getUserId()))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("Content-Type", "application/json")
                .build();

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
     * This method gets the statistic on the speed or emotion reactions.
     * @param isSpeedReaction true:gets speed reaction; false: gets emotion reaction
     * @return an int between -1, 0, 1 representing the statistics of the reaction
     */

    public static int getReactionStats(Boolean isSpeedReaction) {

        // Changes path variable based on the type of reaction requested
        String typeOfReaction = isSpeedReaction ? "speed/" : "emotion/";

        session = session.getInstance();


        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/reactions/stat/"
                        + typeOfReaction
                        + session.getRoomLink()))
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

        assert response != null;
        return Integer.parseInt(response.body());

    }


    /**
     * sends a GET request to the server.
     * should receive a list of counts of each emotion reaction
     * (index 0 - confused, 1 - sad, 2 - happy)
     * @return a list of emotion reaction counts or
     *      a list of (0, 0, 0) if the response code was not 200
     *      or the list from the server was not of size 3.
     */
    public static List<Integer> getAllReactionCount() {
        session = Session.getInstance();

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/reactions/counts/emotion/"
                        + session.getRoomLink()))
                .build();

        System.out.println("Sending request: " + request.toString());

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Status: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(0, 0, 0);
        }

        List<Integer> result = gson.fromJson(response.body(), new TypeToken<List<Integer>>() {
        }.getType());
        if (result.size() != 3) {
            return List.of(0, 0, 0);
        }
        return result;
    }

}
