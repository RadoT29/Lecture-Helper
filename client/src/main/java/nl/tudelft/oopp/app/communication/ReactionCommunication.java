package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.models.EmotionReaction;
import nl.tudelft.oopp.app.models.Reaction;
import nl.tudelft.oopp.app.models.Session;
import nl.tudelft.oopp.app.models.SpeedReaction;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ReactionCommunication {

    private static Gson gson = new Gson();
    private static HttpClient client = HttpClient.newBuilder().build();
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

        int stat = Integer.parseInt(response.body());
        return stat;
    }

}
