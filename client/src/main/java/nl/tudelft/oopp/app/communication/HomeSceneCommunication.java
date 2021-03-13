package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.oopp.app.models.Entry;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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

    /**
     * gets the list of entries from the server.
     * @return a list of entries (question + upvotes)
     */
    public static List<Entry> getQuestions() {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/entry/refresh"))
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
        List<Question> list =  gson.fromJson(response.body(), new TypeToken<List<Question>>(){}.getType());
        return getUpvoteList(list);
    }

    /**
     * finds the upvotes for all questions on a list
     * @param questions List of Questions
     * @return a list of Entries (Question + upvotes)
     */
    private static List<Entry> getUpvoteList(List<Question> questions) {
        List<Entry> result = new ArrayList<>();
        for (Question q : questions) {
            result.add(new Entry (q, getUpvotesForQuestion(q)));
        }
        return result;
    }

    /**
     * finds the number of upvotes for a Question
     * @param question Question for who we look for the upvotes
     * @return int number of upvotes for that question
     *      or -1 if there was an error
     */
    private static int getUpvotesForQuestion(Question question) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/upvote/count?q=" + question.questionID))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), int.class);
    }

}

