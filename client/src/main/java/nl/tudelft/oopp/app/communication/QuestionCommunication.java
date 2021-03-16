package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.models.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class QuestionCommunication {

    private static Gson gson = new Gson();
    private static HttpClient client = HttpClient.newBuilder().build();


    /**
    * send a DELETE request to the server to delete the question.
    * @param questionId the id of a question that is supposed to be deleted
    */
    public static void dismissQuestion(long questionId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/questions/dismiss/" + questionId))
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

    /**
     * Send a POST request to update the number of Upvotes associated to a specific question.
     * @param questionId - question where upvote status is changed
     */
    public static void upVoteQuestion(String questionId) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/questions/changeUpvote/" + questionId + "/" + Session.getInstance().getUserId()))
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
     * send a DELETE request to the server to delete the question.
     * @param questionId the id of a question that is supposed to be deleted
     */
    public static void deleteUpvote(String questionId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/questions/deleteUpvote/" + questionId + "/" + Session.getInstance().getUserId()))
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