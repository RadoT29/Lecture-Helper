package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Poll;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.services.PollModeratorService;
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

    private final PollModeratorService pollModeratorService;

    @Autowired
    public PollController(PollModeratorService pollModeratorService) {
        this.pollModeratorService = pollModeratorService;
    }

    @GetMapping("/{roomLink}")
    @ResponseBody
    public List<Poll> getPolls(@PathVariable String roomLink) {
        return pollModeratorService.getPolls(roomLink);
    }

    @PostMapping("/{roomLink}")
    @ResponseBody
    public long createPoll(@PathVariable String roomLink) {
        return pollModeratorService.createPoll(roomLink);
    }

    @PutMapping("/{roomLink}/{pollId}")
    @ResponseBody
    public void updatePoll(@PathVariable String roomLink,
                         @PathVariable Long pollId,
                         @RequestBody Poll poll) {
        pollModeratorService.updateAndOpenPoll(roomLink, pollId, poll);
    }

    @PatchMapping("/{roomLink}/{pollId}")
    @ResponseBody
    public void closePoll(@PathVariable String roomLink,
                         @PathVariable Long pollId) {
        pollModeratorService.closePoll(roomLink, pollId);
    }

}
