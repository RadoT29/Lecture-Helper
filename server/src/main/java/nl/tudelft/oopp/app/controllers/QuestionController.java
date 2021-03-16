package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.services.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class handles all the Endpoints related to the questions.
 */

@Controller
@RequestMapping("questions")
public class QuestionController {

    private final QuestionService questionService;

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
     * This method parses the path variables and the http request
     * to get the values needed to create the question to be saved on the DB.
     * @param roomLink link of the room where the question has been asked
     * @param userId id of the user who asked the question
     * @param question question that has been asked
     */
    @PostMapping("/{roomLink}/{userId}")
    @ResponseBody
    public void add(@PathVariable String roomLink,
                    @PathVariable String userId,
                    @RequestBody Question question) {
        System.out.println("Question arrived on server!");
        questionService.addNewQuestion(roomLink,userId,question);
    }

    /**
     * calls the questionService to delete the question from the database.
     * @param questionId the id of the question to be deleted
     */
    @DeleteMapping("/dismiss/{questionId}")
    @ResponseBody
    public void dismissQuestion(@PathVariable("questionId") long questionId) {
        questionService.dismissQuestion(questionId);
    }

    /**
     * calls the questionService to add an upvote to a specific question by a specific
     * user on the database.
     * @param questionId the id of the question to be upVoted
     * @param userId - Id of the user who upVoted
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
     * @param userId - user who made the change
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
    public void clearQuestions(@PathVariable ("roomLink") String roomLink) {
        questionService.clearQuestions(roomLink);
    }


}
