package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.QuestionsUpdate;
import nl.tudelft.oopp.app.repositories.QuestionsUpdateRepository;
import nl.tudelft.oopp.app.services.QuestionService;
import nl.tudelft.oopp.app.services.QuestionsUpdateService;
import nl.tudelft.oopp.app.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class handles all the Endpoints related to the questions.
 */

@Controller
@RequestMapping("questions")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    private QuestionsUpdateService questionsUpdateService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private QuestionsUpdateRepository questionsUpdateRepository;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/refresh/{roomLink}")
    @ResponseBody
    public List<Question> getAllQuestions(@PathVariable String roomLink) {
        return questionService.getAllQuestionsByRoom(roomLink);
    }

    /**
     * Method to retrieve the last entry made by a specific user in a specific room.
     * @param roomLink - room where request came from
     * @param userId   - Id of user that made request
     * @return - string of the questionId
     */
    @GetMapping("/getOneQuestion/{roomLink}/{userId}")
    @ResponseBody
    public String getSingularQuestion(@PathVariable String roomLink,
                                      @PathVariable String userId) {
        return questionService.getSingleQuestion(roomLink, userId);

    }


    /**
     * This method parses the path variables and the http request
     * to get the values needed to create the question to be saved on the DB.
     * @param roomLink link of the room where the question has been asked
     * @param userId   id of the user who asked the question
     * @param question question that has been asked
     */
    @PostMapping("/{roomLink}/{userId}")
    @ResponseBody
    public void add(@PathVariable String roomLink,
                    @PathVariable String userId,
                    @RequestBody Question question) {
        questionService.addNewQuestion(roomLink, userId, question);
    }

    /**
     * calls the questionService to delete the question from the database.
     * @param questionId the id of the question to be deleted
     */
    @DeleteMapping("/dismiss/{questionId}")
    @ResponseBody
    public void dismissQuestion(@PathVariable("questionId") long questionId) {
        Question question = questionService.findByQuestionId(questionId);
        questionService.dismissQuestion(questionId);
        QuestionsUpdate update = new QuestionsUpdate(question.getUser(), question.getRoom(), -1);
        update.setQuestionText(question.getQuestionText());
        questionsUpdateRepository.save(update);
    }


    @DeleteMapping("/dismissSingular/{questionId}/{userId}")
    @ResponseBody
    public void dismissSingular(@PathVariable("questionId") long questionId,
                                @PathVariable("userId") long userId) {
        questionService.dismissSingular(questionId, userId);
    }



    /**
     * calls the questionService to add an upvote to a specific question by a specific
     * user on the database.
     * @param questionId the id of the question to be upVoted
     * @param userId     - Id of the user who upVoted
     */
    @PostMapping("/changeUpvote/{questionId}/{userId}")
    @ResponseBody
    public void addUpvote(@PathVariable String questionId,
                          @PathVariable String userId) {
        System.out.println("Upvote arrived on server!");

        questionService.addUpvote(questionId, userId);

    }

    /**
     * Receives the DELETE request from the client side
     * calls the questionService to delete the upvote from the database.
     * @param questionId the id of the question to be deleted
     * @param userId     - user who made the change
     */
    @DeleteMapping("/deleteUpvote/{questionId}/{userId}")
    @ResponseBody
    public void deleteUpvote(@PathVariable("questionId") String questionId,
                             @PathVariable("userId") String userId) {
        questionService.deleteUpvote(questionId, userId);
    }

    /**
     * Receives DELETE request made from the client side
     * calls the questionService so to delete all the questions made in that room.
     * Receives the roomLink from which the request was made
     * The roomLink is unique so it can be used as an identifier for the room
     */
    @DeleteMapping("/clearAllQuestions/{roomLink}")
    @ResponseBody
    public void clearQuestions(@PathVariable("roomLink") String roomLink) {
        questionService.clearQuestions(roomLink);
    }

    /**
     * Gets all questions in a room and sends them to client.
     * The function is asynchronous.
     *
     * @return - Other.
     */
    @GetMapping("/constant/{roomLink}")
    @ResponseBody
    public DeferredResult<List<Question>> sendAllQuestionsAsync(@PathVariable String roomLink) {
        Long timeOut = 100000L;
        String timeOutResp = "Time out.";
        DeferredResult<List<Question>> deferredResult = new DeferredResult<>(timeOut, timeOutResp);
        CompletableFuture.runAsync(() -> {
            List<Question> newQuestions = questionService.getAllQuestionsByRoom(roomLink);
            deferredResult.setResult(newQuestions);
        });
        return deferredResult;
    }

    /**
     * Gets all questions in a room and sends them to client.
     * The function is asynchronous.
     * @return - Other.
     */
    @GetMapping("/log/{roomLink}")
    @ResponseBody
    public DeferredResult<List<Question>>
        sendAllAnsweredQuestionsAsync(@PathVariable String roomLink) {
        Long timeOut = 100000L;
        String timeOutResp = "Time out.";
        DeferredResult<List<Question>> deferredResult = new DeferredResult<>(timeOut,timeOutResp);
        CompletableFuture.runAsync(() -> {
            List<Question> newQuestions = questionService.getAllAnsweredQuestions(roomLink);
            deferredResult.setResult(newQuestions);
        });

        return deferredResult;
    }



    /**
     * Receives a POST request from the client.
     * calls questionService to change the text of a question
     * @param questionId String from PathVariable, id of the question to be modified
     * @param newText    String from RequestBody, new text for the question
     */
    @PostMapping("/edit/{questionId}")
    @ResponseBody
    public void editQuestionText(@PathVariable String questionId,
                                 @RequestBody String newText) {
        long questionId2 = Long.parseLong(questionId);
        //remove quotation marks from the newText
        newText = newText.substring(1, newText.length() - 1);
        questionService.editQuestionText(questionId2, newText);
    }

    /**
     * Receives a POST request from the client with the new answer to be set.
     * calls set answer to create an instance of the answer.
     *
     * @param questionId      String from PathVariable, id of the question to be answered
     * @param userId          - id of the moderator that set the answer
     * @param answeredInClass - type of answer created
     */
    @PostMapping("/answer/setAsAnswered/{questionId}/{userId}/{answeredInClass}")
    @ResponseBody
    public void setAnswered(@PathVariable String questionId,
                            @PathVariable String userId,
                            @PathVariable boolean answeredInClass) {

        String answerText = "";

        if (answeredInClass) {
            answerText = "This question was answered during the lecture";
        }
        Question question = questionService.findByQuestionId(Long.parseLong(questionId));
        questionService.setAnswered(answerText, questionId, userId, answeredInClass);
        QuestionsUpdate update = new QuestionsUpdate(question.getUser(), question.getRoom(), 0);
        update.setQuestionText(question.getQuestionText());
        questionsUpdateRepository.save(update);

    }


    /**
     * Receive GET request from client so to chek if a question is answered.
     * @param questionId - id of question to check
     * @param roomLink - room where question is
     * @return - boolean if answered
     */
    @GetMapping("/answer/checkAnswer/{questionId}/{roomLink}")
    @ResponseBody
    public boolean checkAnswered(@PathVariable("questionId") String questionId,
                                 @PathVariable("roomLink") String roomLink) {

        return questionService.checkAnswered(questionId, roomLink);
    }

    /**
     * receive GET request to export the questions.
     * @param roomLink - room from where to export
     * @return String with all the questions and answers (formatted)
     */
    @GetMapping("/export/{roomLink}")
    @ResponseBody
    public String exportQuestions(@PathVariable("roomLink") String roomLink) {
        return questionService.exportQuestions(roomLink);

    }

    /**
     * Method returns an updateQuestion object
     * containing data for the question update.
     *
     * @param userId   - user id
     * @param roomLink - room link
     * @return - the update question info as status and question text.
     */
    @GetMapping(path = "/updateOnQuestion/{userId}/{roomLink}")
    @ResponseBody
    public QuestionsUpdate updateOnQuestion(@PathVariable("userId") String userId,
                                            @PathVariable("roomLink") String roomLink) {
        Long idUser = Long.valueOf(userId);
        Long idRoom = roomService.getByLink(roomLink).getId();
        QuestionsUpdate update = questionsUpdateService.findUpdate(idUser, idRoom);
        if (update == null) {
            return null;
        }
        questionsUpdateService.deleteUpdate(update.getId(), idUser, idRoom);

        return update;
    }


    /**
     * Saves a new answer.
     * @param questionId - the question id.
     * @param userId - the user id.
     * @param newText - the text.
     */
    @PostMapping("/setAnswer/{questionId}/user/{userId}")
    @ResponseBody
    public void setAnswerText(@PathVariable String questionId,
                              @PathVariable String userId,
                              @RequestBody String newText) {

        //remove quotation marks from the newText
        newText = newText.substring(1, newText.length() - 1);
        questionService.setAnswered(newText,questionId,userId,false);
    }


    @GetMapping("/upVote/getModUpVotes/{questionId}/{roomLink}")
    @ResponseBody
    public int getModUpVotes(@PathVariable("questionId") String questionId,
                                @PathVariable("roomLink") String roomLink) {
        return questionService.getModUpVotes(questionId, roomLink);

    }

}
