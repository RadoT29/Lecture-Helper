package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.oopp.app.exceptions.OutOfLimitOfQuestionsException;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


/**
 * This class contains the interactions between the home page on the client and the server.
 */
public class HomeSceneCommunication {

    private static final Gson gson = new Gson();
    private static Session session;

    private static final HttpClient client = HttpClient.newBuilder().build();
    private static HttpResponse<String> response;

    /**
     * This method makes a POST Request on the server with the question object as body
     * and the roomLink and userId as path variables
     * so that the question is associated with the right room and user.
     * @param question the question to be saved on the database
     */

    public static void postQuestion(Question question) {
        String requestBody = gson.toJson(question);
        session = Session.getInstance();

        //Creates a new POST Request at the link question/roomLink/userId
        // with the Question object as body
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/questions/" + session.getRoomLink() + "/" + session.getUserId()))
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
     * Gets list of questions from the server specific to this room link.
     * (all of the questions user's IDs and room ID are already set to zero)
     * @return a list questions
     */
    public static List<Question> getQuestions() {
        session = Session.getInstance();
        System.out.println("http://localhost:8080/questions/refresh/" + session.getRoomLink());
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/questions/refresh/" + session.getRoomLink()))
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        System.out.println(response.body());
        return gson.fromJson(response.body(), new TypeToken<List<Question>>() {
        }.getType());
    }


    /**
     * GET request to get the id of the question that was just created and sent to
     * the server. So to have store its ID locally.
     * @return a String questionID
     */
    public static Long getSingleQuestion() {
        session = Session.getInstance();
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/questions/getOneQuestion/" + session.getRoomLink() + "/" + session.getUserId()))
                .build();

        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        return gson.fromJson(response.body(), Long.class);
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
     * Sends a request to get all questions from a room.
     * @param roomLink - the link for the room.
     * @return - A List of all questions.
     * @throws InterruptedException - Thrown when a thread is waiting and is interrupted.
     */
    public static List<Question> constantlyGetQuestions(String roomLink)
            throws InterruptedException {

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/questions/constant/" + roomLink))
                .build();

        return tryCatchResponseQuestionsList(request);
    }


    /**
     * Sends a request to get all answered questions from a room.
     * @param roomLink - the room link.
     * @return - a list of answered questions.
     * @throws InterruptedException - may be thrown.
     */
    public static List<Question> constantlyGetAnsweredQuestions(String roomLink)
            throws InterruptedException {

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/questions/log/" + roomLink))
                .build();

        return tryCatchResponseQuestionsList(request);

    }

    private static List<Question> tryCatchResponseQuestionsList(HttpRequest request)
            throws InterruptedException {

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Status: " + response.statusCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }

        return gson.fromJson(response.body(), new TypeToken<List<Question>>(){}.getType());
    }


    /**
     * Gets an export of the questions and answers in total.
     * (all of the questions user's IDs and room ID are already set to zero)
     * @return a list questions
     */
    public static String exportQuestions(String roomLink) {

        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/questions/export/" + roomLink))
                .build();

        try {
            response = client.send(request,HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }

        return response.body();
    }

    /**
     * This method make a request to the server if the user can ask a question.
     * If the user has passed the limit the method throw exception
     * and the question is not processed.
     * @param userId - the user id
     * @param roomLink - the student link for the room. With that link is found the room id.
     * @throws OutOfLimitOfQuestionsException - This exception is thrown if the user has passed the
     *      limit of questions that can ask.
     */
    public static void isInLimitOfQuestion(String userId, String roomLink)
            throws OutOfLimitOfQuestionsException {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/room/user/canAskQuestion/" + userId + "/" + roomLink)).build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
        System.out.println(response.body());
        boolean result = gson.fromJson(response.body(), Boolean.class);
        if (!result) {
            throw new OutOfLimitOfQuestionsException("The user has passed the"
                    + "limit of questions for the set time");
        }

    }

    /**
     * This method sends request in which are contained the the limit of questions per time.
     * @param numQuestions - the number of questions
     * @param minutes - the period of time
     * @param roomLink - the room id then is found by that link
     */
    public static void setQuestionsPerTime(int numQuestions, int minutes, String roomLink) {

        HttpRequest request = HttpRequest.newBuilder().PUT(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/setConstraints/" + roomLink + "/" + numQuestions + "/" + minutes))
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
        if (response.statusCode() != 200) {
            System.out.println("Status: " + response.statusCode());
        }
    }
}

