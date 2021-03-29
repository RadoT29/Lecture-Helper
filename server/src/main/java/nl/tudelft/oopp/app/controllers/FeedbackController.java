package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Feedback;
import nl.tudelft.oopp.app.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * handles the request for adding new feedback to a specified room.
     * @param roomLink String link of the room that receives feedback
     * @param feedback Feedback passed by RequestBody to be saved
     */
    @PostMapping("/{roomLink}")
    @ResponseBody
    public void addFeedback(@PathVariable String roomLink,
                            @RequestBody Feedback feedback) {
        feedbackService.addFeedback(roomLink, feedback);
    }
}
