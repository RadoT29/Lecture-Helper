package nl.tudelft.oopp.app.communication;

import com.google.gson.Gson;
import nl.tudelft.oopp.app.controllers.StudentSceneController;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.QuestionsUpdate;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class QuestionCommunicationTest {
    private ClientAndServer mockServer;
    private Gson gson = new Gson();
    private Room room;
    private long userId;
    private Question question;
    private Session session;

    /**Start the mock server before each test.
     */
    @BeforeEach
    public void startMockServer() {
        mockServer = startClientAndServer(8080);

        userId = 1L;
        question = new Question("questionText");
        question.setAnswered(true);
        room = new Room("roomName456");
        UUID roomLink = UUID.randomUUID();
        room.setLinkIdModerator(roomLink);

        session = Session.getInstance(room.getLinkIdModerator().toString(),
                room.getName(), String.valueOf(userId), true);


    }

    /**Close the mock server after each test so they are completely isolated.
     * Clear the sessions singleton for the same reason.
     */
    @AfterEach
    public void stopMockServer() {
        Session.clearSessionTest();
        mockServer.stop();
    }

    /**Test if QuestionCommunication.dismissQuestion sends
     *  a delete question request to the right path.
     */
    @Test
    void shouldSendDismissQuestionRequest() {

        QuestionCommunication.dismissQuestion(question.getId());

        String path = "/questions/dismiss/" + question.getId();
        mockServer.verify(request()
                .withMethod("DELETE")
                .withPath(path));
    }

    /**Test if QuestionCommunication.dismissSingular sends
     *  a delete question request to the right path.
     */
    @Test
    void shouldSendDismissQuestionRequestFromStudent() {
        QuestionCommunication.dismissSingular(question.getId(), userId);

        String path = "/questions/dismissSingular/" + question.getId() + '/' + userId;
        mockServer.verify(request()
                .withMethod("DELETE")
                .withPath(path));
    }

    /**Test if QuestionCommunication.upVoteQuestion sends
     *  a post upvote request to the right path.
     */
    @Test
    void shouldSendUpVoteRequest() {

        QuestionCommunication.upVoteQuestion(String.valueOf(question.getId()));

        String path = "/questions/changeUpvote/" + question.getId() + '/' + userId;
        mockServer.verify(request()
                .withMethod("POST")
                .withPath(path));
    }

    /**Test if QuestionCommunication.deleteUpvote sends
     *  a delete upvote request to the right path.
     */
    @Test
    void shouldSendDeleteUpVoteRequest() {
        QuestionCommunication.deleteUpvote(String.valueOf(question.getId()));

        String path = "/questions/deleteUpvote/" + question.getId() + '/' + userId;
        mockServer.verify(request()
                .withMethod("DELETE")
                .withPath(path));
    }

    /**Test if QuestionCommunication.editQuestionText sends
     *  a post edit question request to the right path.
     */
    @Test
    void shouldSendEditQuestionRequest() {
        String questionText = "questiooon";

        QuestionCommunication.editQuestionText(String.valueOf(question.getId()), questionText);

        String path = "/questions/edit/" + question.getId();
        mockServer.verify(request()
                .withMethod("POST")
                .withPath(path)
                .withBody(gson.toJson(questionText)));
    }


    @Test
    void shouldCheckAnswered() {
        mockCheckAnswered(question.getId());
        Boolean answered = QuestionCommunication.checkAnswered(String.valueOf(question.getId()));
        assertTrue(answered);
    }

    void mockCheckAnswered(long questionId) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/questions/answer/checkAnswer/" + questionId + "/"
                                + session.getRoomLink())
        )
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody("true"));

    }


    @Test
    void shouldSetAnswered() {

        QuestionCommunication.setAnswered(String.valueOf(question.getId()), true);

        String path = "/questions/answer/setAsAnswered/" + question.getId() + "/"
                + session.getUserId() + "/" + true;
        mockServer.verify(request()
                .withMethod("POST")
                .withPath(path));
    }


    @Test
    void shouldCheckForUpdatesFalse() {
        String path = "/questions/updateOnQuestion/" + userId + "/" + room.getLinkIdModerator();

        QuestionCommunication.updatesOnQuestions(String.valueOf(userId),
                room.getLinkIdModerator().toString());
        mockServer.verify(request()
                .withMethod("GET")
                .withPath(path));
    }

    @Test
    void shouldAddAnswerText() {
        String path = "/questions/setAnswer/" + question.getId() + "/user/" + userId;
        QuestionCommunication.addAnswerText(String.valueOf(question.getId()),
                "text", String.valueOf(userId));
        mockServer.verify(request()
                .withMethod("POST")
                .withPath(path));
    }
}
