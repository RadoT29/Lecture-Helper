package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Poll;
import nl.tudelft.oopp.app.models.PollAnswer;
import nl.tudelft.oopp.app.services.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class handles all the Endpoints related to the questions.
 */

@Controller
@RequestMapping("polls")
public class PollController {

    private final PollService pollService;

    @Autowired
    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @GetMapping("/{roomLink}")
    @ResponseBody
    public List<Poll> getPolls(@PathVariable String roomLink) {
        return pollService.getPolls(roomLink);
    }

    @GetMapping("/constant/{roomLink}")
    @ResponseBody
    public DeferredResult<List<Poll>> sendAllPollsAsync(@PathVariable String roomLink) {
        Long timeOut = 100000L;
        String timeOutResp = "Time out.";
        DeferredResult<List<Poll>> deferredResult = new DeferredResult<>(timeOut,timeOutResp);
        CompletableFuture.runAsync(() -> {
            List<Poll> newQuestions = pollService.getPolls(roomLink);
            deferredResult.setResult(newQuestions);
        });

        return deferredResult;
    }

    //Moderator routes
    @PostMapping("/{roomLink}")
    @ResponseBody
    public long createPoll(@PathVariable String roomLink) {
        return pollService.createPoll(roomLink);
    }

    @PutMapping("/{roomLink}/{pollId}")
    @ResponseBody
    public void updatePoll(@PathVariable String roomLink,
                         @PathVariable Long pollId,
                         @RequestBody Poll poll) {
        pollService.updateAndOpenPoll(roomLink, pollId, poll);
    }

    @PutMapping("/{roomLink}/{pollId}/close")
    @ResponseBody
    public void closePoll(@PathVariable String roomLink,
                         @PathVariable Long pollId) {
        pollService.closePoll(roomLink, pollId);
    }

    //StudentRoutes
    @GetMapping("/answer/{userId}/{pollId}")
    @ResponseBody
    public List<PollAnswer> getAnswers(@PathVariable Long pollId, @PathVariable Long userId) {
        return pollService.getAnswers(pollId, userId);
    }

    @PostMapping("/answer/{userId}")
    @ResponseBody
    public void createAnswers(@PathVariable Long userId,
                           @RequestBody Poll poll) {
        pollService.createAnswers(userId, poll);
    }

}
