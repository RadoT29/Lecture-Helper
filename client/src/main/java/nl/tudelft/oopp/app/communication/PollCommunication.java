package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.oopp.app.models.Poll;
import nl.tudelft.oopp.app.models.PollAnswer;
import nl.tudelft.oopp.app.models.Session;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class PollCommunication {

    private static final Gson gson = new Gson();
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static HttpResponse<String> response;
    private static Session session;

    /**
     * Method to get polls from the server.
     * Can be used by student and moderator
     * @return list of all the room polls
     */
    public static List<Poll> getPolls() {
        session = Session.getInstance();
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/polls/" + session.getRoomLink()))
                .build();

        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), new TypeToken<List<Poll>>(){}.getType());

    }

    /**
     * Method to get polls from the server.
     * Can be used by student and moderator
     * @param roomLink - the link for the room.
     * @return - A List of all questions.
     * @throws InterruptedException - Thrown when a thread is waiting and is interrupted.
     */
    public static List<Poll> constantlyGetPolls(String roomLink)
            throws InterruptedException {

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/polls/constant/" + roomLink))
                .build();

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

    /**
     * Method send a request to the server to create a new empty poll.
     * @return new poll's id
     */
    public static long createPoll() {
        session = Session.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/polls/" + session.getRoomLink()))
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Status: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return gson.fromJson(response.body(), long.class);
    }

    /**Method sends a request to the server to update a given poll.
     * For now it also automatically opens the poll.
     * @param pollId the id of the poll to be updated
     * @param poll a Poll object with the data of the update
     */
    public static void updatePoll(long pollId, Poll poll) {

        session = Session.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(poll)))
                .uri(URI.create("http://localhost:8080/polls/" + session.getRoomLink() + '/' + pollId))
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
     * Set a poll as finished so students can no longer answer
     * and students and moderators can see the results.
     * @param pollId id of the poll to be finished
     */
    public static void finishPoll(long pollId) {

        session = Session.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/polls/" + session.getRoomLink() + '/' + pollId + "/finish"))
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

    //Student routes

    /**
     * Get from the server the students answers for a given poll.
     * The student id is stored in Session
     * @param pollId Id of the poll
     * @return list of poll answers with the answer of each poll option
     */
    public static List<PollAnswer> getAnswers(long pollId) {

        session = Session.getInstance();
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/polls/answer/" + session.getUserId() + '/' + pollId))
                .build();

        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        return gson.fromJson(response.body(),
                new TypeToken<List<PollAnswer>>(){}.getType());

    }

    /**
     * Send the student answers to the server.
     * It sends the answers as if they were options,
     * the server is responsible for interpreting it the right way.
     * @param poll Poll with poll answers
     */
    public static void sendAnswers(Poll poll) {

        session = Session.getInstance();
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(poll)))
                .uri(URI.create("http://localhost:8080/polls/answer" + '/' + session.getUserId()))
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
}
