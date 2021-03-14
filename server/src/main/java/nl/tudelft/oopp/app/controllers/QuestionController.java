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


}
