package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.models.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class QuestionCommunication {

    private static Gson gson = new Gson();
    private static Session session = Session.getInstance();
    private static HttpClient client = HttpClient.newBuilder().build();


    /**
     * send a DELETE request to the server to delete the question.
     * @param id the id of a question that is supposed to be deleted
     */
    public static void dismissQuestion(long id) {
        String requestBody = gson.toJson(id);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/questions/dismiss/" + id + "/" + session.getUserId()))
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
