package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.oopp.app.models.Poll;
import nl.tudelft.oopp.app.models.PollAnswer;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class PollCommunication {

    private static Gson gson = new Gson();
    private static HttpClient client = HttpClient.newBuilder().build();

    private static Session session = Session.getInstance();

    /**
     * GET request to check if the question has an answer stored on the server.
     *
     * @return a string with true if answered and false if not
     */
    public static List<Poll> getPolls() {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/polls/" + session.getRoomLink()))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        List<Poll> polls = gson.fromJson(response.body(), new TypeToken<List<Poll>>(){}.getType());
        return polls;
    }

    public static List<Poll> constantlyGetPolls(String roomLink)
            throws InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/polls/constant/" + roomLink))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Status: " + response.statusCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
        return gson.fromJson(response.body(), new TypeToken<List<Poll>>(){}.getType());
    }

    //Moderator routes
    public static long createPoll() {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/polls/" + session.getRoomLink()))
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
        long pollId = gson.fromJson(response.body(), long.class);
        return pollId;
    }

    public static void updatePoll(long pollId, Poll poll) {
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(poll)))
                .uri(URI.create("http://localhost:8080/polls/" + session.getRoomLink() + '/' + pollId))
                .setHeader("Content-Type", "application/json")
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

    public static void finishPoll(long pollId) {
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/polls/" + session.getRoomLink() + '/' + pollId + "/finish"))
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

    //Student routes
    public static List<PollAnswer> getAnswers(long pollId) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/polls/answer" + session.getUserId() + '/' + pollId))
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        List<PollAnswer> answers = gson.fromJson(response.body(),
                new TypeToken<List<PollAnswer>>(){}.getType());
        return answers;
    }

    public static void sendAnswers(Poll poll) {
        System.out.println(gson.toJson(poll));
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(poll)))
                .uri(URI.create("http://localhost:8080/polls/answer" + '/' + session.getUserId()))
                .setHeader("Content-Type", "application/json")
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
