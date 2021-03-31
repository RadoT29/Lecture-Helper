package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nl.tudelft.oopp.app.models.Session;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;

public class QuestionCommunication {

    private static Gson gson = new Gson();
    private static HttpClient client = HttpClient.newBuilder().build();

    private static Session session = Session.getInstance();

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
     * send a DELETE request to the server to delete the question by a student
     * (also sends the ID of the student).
     * The student delete request is separated from the normal delete since if
     * those were made through the same request it could create a breach opportunity
     * (for the student to delete questions that are not theirs)
     * @param questionId the id of a question that is supposed to be deleted
     */
    public static void dismissSingular(long questionId, long userId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/questions/dismissSingular/" + questionId + "/" + userId))
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

    /**
     * Sends a POST request to the server to edit the text of a specific question.
     * New text is sent in the request body.
     * @param questionId String - id of the question to be modified
     * @param newText String - new text for the question
     */
    public static void editQuestionText(String questionId, String newText) {
        String requestBody = gson.toJson(newText);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/questions/edit/" + questionId))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("Content-Type", "text/plain")
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
     * GET request to check if the question has an answer stored on the server.
     *
     * @return a string with true if answered and false if not
     */
    public static boolean checkAnswered(String questionId) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/questions/answer/checkAnswer/" + questionId + "/" + session.getRoomLink()))
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
        return gson.fromJson(response.body(), Boolean.class);
    }



    /**
     * Send a POST request to update the answer status of a question.
     * the boolean createdInclass establishes if the answer was
     * made in the comments or directly.
     * @param questionId - question where upvote status is changed
     */
    public static void setAnswered(String questionId, boolean createdInClass) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://localhost:8080/questions/answer/setAsAnswered/" + questionId + "/"
                        + session.getUserId() + "/" + createdInClass))
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
     * Makes a request to store the answer in the repository.
     * @param questionId - the question id.
     * @param newText - the answer.
     * @param userId - the user id.
     */
    public static void addAnswerText(String questionId, String newText, String userId) {

        String requestBody = gson.toJson(newText);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/questions/setAnswer/" + questionId + "/user/" + userId))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .setHeader("Content-Type", "text/plain")
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
     * GET request to check if the question has an answer stored on the server.
     *
     * @return a string with true if answered and false if not
     */
    public static int getModUpVotes(long questionId) {
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("http://localhost:8080/questions/upVote/getModUpVotes/" + questionId + "/"
                        + session.getRoomLink()))
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
        return gson.fromJson(response.body(), int.class);
    }

}
