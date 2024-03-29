package nl.tudelft.oopp.app.communication;

import nl.tudelft.oopp.app.exceptions.NoSuchRoomException;
import nl.tudelft.oopp.app.exceptions.OutOfLimitOfQuestionsException;
import nl.tudelft.oopp.app.models.Answer;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class HomeSceneCommunicationTest {
    private ClientAndServer mockServer;
    private String roomLink;
    private String roomName;
    private String userId;
    private Question question1;
    private Question question2;
    private List<Question> questions;
    private PrintStream standardOut = System.out;
    private ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    /**
     * Start the mock server before each test.
     */
    @BeforeEach
    public void startMockServer() {
        mockServer = startClientAndServer(8080);
        System.setOut(new PrintStream(outputStreamCaptor));

        roomLink = UUID.randomUUID().toString();
        roomName = "roomName456";
        userId = UUID.randomUUID().toString();

        Session.getInstance(roomLink, roomName, userId, true);

        question1 = new Question("questionText1");
        question2 = new Question("questionText2");
        questions = new ArrayList<Question>();
        question1.setAnswered(true);
        question2.setAnswered(true);
        questions.add(question1);
        questions.add(question2);
    }

    /**Close the mock server after each test so they are completely isolated.
     * Clear the sessions singleton for the same reason.
     */
    @AfterEach
    public void stopMockServer() {
        System.setOut(standardOut);
        Session.clearSessionTest();
        mockServer.stop();
    }

    /**
     * Test if HomeSceneCommunication.postQuestion
     * sends a post question request to the right path.
     */
    @Test
    void shouldSendQuestionRequest() {
        String roomLink = UUID.randomUUID().toString();
        String roomName = "roomName456";
        String userId = UUID.randomUUID().toString();

        Session session = Session.getInstance(roomLink, roomName, userId, true);
        HomeSceneCommunication.postQuestion(new Question());

        String path = "/questions/" + roomLink + '/' + userId;
        mockServer.verify(request()
                .withMethod("POST")
                .withPath(path));
        Session.clearSessionTest();
    }

    /**Method to set the mock server for the proper request/response.
     */
    void mockGetQuestions(
            List<Question> questions, String roomLink, String roomName, String userId,
            int statusCode
    ) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/questions/refresh/" + roomLink)
        )
                .respond(
                        response()
                                .withStatusCode(statusCode)
                                .withBody(CommuncationResponses.getQuestionsBodyResponse(
                                                questions, roomLink, roomName, userId)));
    }

    /**Test if HomeSceneCommunication.getQuestions() return the questions
     */
    @Test
    void shouldGetQuestions() {

        mockGetQuestions(questions, roomLink, roomName, userId, 200);

        List<Question> retrievedQuestions = HomeSceneCommunication.getQuestions();

        assertEquals(retrievedQuestions.get(0).getQuestionText(), question1.getQuestionText());
        assertEquals(retrievedQuestions.get(1).getQuestionText(), question2.getQuestionText());
        assertEquals(retrievedQuestions.get(0).getRoom().getLinkIdModerator().toString(), roomLink);
    }


    @Test
    void shouldGetQuestionsError() {

        mockGetQuestions(questions, roomLink, roomName, userId, 300);

        List<Question> retrievedQuestions = HomeSceneCommunication.getQuestions();
        assertTrue(outputStreamCaptor.toString()
                .trim().contains("Status: 300"));

    }


    /**Method to set the mock server for the proper request/response.
     */
    void mockGetSingleQuestion(String roomLink, String userId, Long questionId, int statusCode) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/questions/getOneQuestion/" + roomLink + '/' + userId)
        )
                .respond(
                        response()
                                .withStatusCode(statusCode)
                                .withBody(questionId.toString()));
    }

    /**
     * Test if HomeSceneCommunication.getSingleQuestion() return the question from the user
     */
    @Test
    void shouldGetSingleQuestions() {
        String roomLink = UUID.randomUUID().toString();
        String roomName = "roomName456";
        String userId = UUID.randomUUID().toString();

        Session.getInstance(roomLink, roomName, userId, true);

        Long questionId = 1L;
        mockGetSingleQuestion(roomLink, userId, questionId, 200);

        Long receivedLong = HomeSceneCommunication.getSingleQuestion();

        assertEquals(questionId, receivedLong);
        Session.clearSessionTest();
    }

    @Test
    void shouldGetSingleQuestionsError() {
        String roomLink = UUID.randomUUID().toString();
        String roomName = "roomName456";
        String userId = UUID.randomUUID().toString();

        Session.getInstance(roomLink, roomName, userId, true);

        Long questionId = 1L;
        mockGetSingleQuestion(roomLink, userId, questionId, 300);
        HomeSceneCommunication.getSingleQuestion();

        assertTrue(outputStreamCaptor.toString()
                .trim().contains("Status: 300"));
        Session.clearSessionTest();
    }



    /**Test if HomeSceneCommunication.clearQuestions sends
     *  a delete questions request to the right path.
     */
    @Test
    void shouldSendClearQuestionsRequest() {
        String roomLink = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        HomeSceneCommunication.clearQuestions(roomLink);

        String path = "/questions/clearAllQuestions/" + roomLink;
        mockServer.verify(request()
                .withMethod("DELETE")
                .withPath(path));
    }


    @Test
    void shouldConstantlyGetQuestions() throws InterruptedException {

        mockConstantlyGetQuestions(questions, roomLink, roomName, userId, 200);

        List<Question> retrievedQuestions = HomeSceneCommunication.constantlyGetQuestions(roomLink);

        assertEquals(retrievedQuestions.get(0).getQuestionText(), question1.getQuestionText());
    }


    @Test
    void shouldConstantlyGetQuestionsError() throws InterruptedException {

        mockConstantlyGetQuestions(questions, roomLink, roomName, userId, 300);

        List<Question> retrievedQuestions = HomeSceneCommunication.constantlyGetQuestions(roomLink);

        assertTrue(outputStreamCaptor.toString()
                .trim().contains("Status: 300"));
    }



    void mockConstantlyGetQuestions(
            List<Question> questions, String roomLink, String roomName, String userId,
            int statusCode
    ) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/questions/constant/" + roomLink)
        )
                .respond(
                        response()
                                .withStatusCode(statusCode)
                                .withBody(CommuncationResponses.getQuestionsBodyResponse(
                                        questions, roomLink, roomName, userId)));
    }

    @Test
    void shouldExportQuestions() {
        mockExportQuestions(questions, roomLink, roomName, userId, 200);

        String exported = HomeSceneCommunication.exportQuestions(roomLink);
        String expected = "Questions and Answers from Room: sala 17\n"
                + "\n"
                + "Question: \n"
                + "0:0:28: question1\n"
                + "Answers:\n"
                + "0:0:35: This question was answered during the lecture\n"
                + "\n"
                + "Question: \n"
                + "0:0:31: question2\n"
                + "This question was not answered yet\n"
                + "\n";
        assertEquals(expected, exported);
    }

    @Test
    void shouldExportQuestionsError() {
        mockExportQuestions(questions, roomLink, roomName, userId, 300);

        HomeSceneCommunication.exportQuestions(roomLink);
        assertTrue(outputStreamCaptor.toString()
                .trim().contains("Status: 300"));
    }

    void mockExportQuestions(
            List<Question> questions, String roomLink, String roomName, String userId,
            int statusCode) {
        {
            mockServer.when(
                    request()
                            .withMethod("GET")
                            .withPath("/questions/export/" + roomLink)
            )
                    .respond(
                            response()
                                    .withStatusCode(statusCode)
                                    .withBody(CommuncationResponses
                                            .getExportedQuestionsBodyResponse(
                                                    questions, roomLink, roomName, userId)));
        }
    }

    @Test
    void shouldGetConstantlyAnsweredQuestions() throws InterruptedException {
        mockConstantlyGetAnsweredQuestions(questions, roomLink, roomName, userId, 200);

        List<Question> retrievedQuestions = HomeSceneCommunication
                                        .constantlyGetAnsweredQuestions(roomLink);

        assertEquals(2, retrievedQuestions.size());
    }

    @Test
    void shouldGetConstantlyAnsweredQuestionsError() throws InterruptedException {
        mockConstantlyGetAnsweredQuestions(questions, roomLink, roomName, userId, 300);

        HomeSceneCommunication.constantlyGetAnsweredQuestions(roomLink);

        assertTrue(outputStreamCaptor.toString()
                .trim().contains("Status: 300"));
    }




    void mockConstantlyGetAnsweredQuestions(
            List<Question> questions, String roomLink, String roomName, String userId,
            int statusCode
    ) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/questions/log/" + roomLink)
        )
                .respond(
                        response()
                                .withStatusCode(statusCode)
                                .withBody(CommuncationResponses.getQuestionsBodyResponse(
                                        questions, roomLink, roomName, userId)));
    }







    @Test
    void isInLimitOfQuestionsTestTrue() {
        mockIsInLimitOfQuestion(true, 200);

        assertDoesNotThrow(() -> {
            HomeSceneCommunication.isInLimitOfQuestion(userId, roomLink);
        }
        );
    }

    @Test
    void isInLimitOfQuestionsTestFalse() {
        mockIsInLimitOfQuestion(false, 200);

        assertThrows(OutOfLimitOfQuestionsException.class, () -> {
            HomeSceneCommunication.isInLimitOfQuestion(userId, roomLink);
        });
    }

    @Test
    void isInLimitOfQuestionsTestError() throws OutOfLimitOfQuestionsException {
        mockIsInLimitOfQuestion(true, 300);
        HomeSceneCommunication.isInLimitOfQuestion(userId, roomLink);

        assertTrue(outputStreamCaptor.toString()
                .trim().contains("Status: 300"));
    }



    void mockIsInLimitOfQuestion(boolean check, int statusCode) {
        {
            mockServer.when(
                    request()
                            .withMethod("GET")
                            .withPath("/room/user/canAskQuestion/" + userId + "/" + roomLink)
            )
                    .respond(
                            response()
                                    .withStatusCode(statusCode)
                                    .withBody(String.valueOf(check)));
        }
    }

    @Test
    void shouldSetQuestionsPerTime() {
        HomeSceneCommunication.setQuestionsPerTime(3,1, roomLink);
        String path = "/setConstraints/" + roomLink + "/" + 3 + "/" + 1;
        mockServer.verify(request()
                .withMethod("PUT")
                .withPath(path));

    }

}
