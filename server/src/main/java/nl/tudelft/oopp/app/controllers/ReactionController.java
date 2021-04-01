package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.EmotionReaction;
import nl.tudelft.oopp.app.models.SpeedReaction;
import nl.tudelft.oopp.app.services.ReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class handles all the Endpoints related to the reactions.
 */

@Controller
@RequestMapping("reactions")
public class ReactionController {

    private final ReactionService reactionService;

    @Autowired
    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }


    /**
     * This method parses the path variables and the http request
     * to get the values needed to create the SpeedReaction that has to be saved on the DB.
     * @param roomLink link of the room where the reaction has been created
     * @param userId id of the user who reacted
     * @param reaction SpeedReaction that has been sent
     */
    @PostMapping("speed/{roomLink}/{userId}")
    @ResponseBody
    public void add(@PathVariable String roomLink,
                    @PathVariable String userId,
                    @RequestBody SpeedReaction reaction) {

        reactionService.addNewReaction(roomLink, userId, reaction);

    }

    /**
     * This method parses the path variables and the http request
     * to get the values needed to create the EmotionReaction that has to be saved on the DB.
     * @param roomLink link of the room where the reaction has been created
     * @param userId id of the user who reacted
     * @param reaction EmotionReaction that has been sent
     */
    @PostMapping("emotion/{roomLink}/{userId}")
    @ResponseBody
    public void add(@PathVariable String roomLink,
                    @PathVariable String userId,
                    @RequestBody EmotionReaction reaction) {

        reactionService.addNewReaction(roomLink, userId, reaction);

    }

    /**
     * This method return the speed statistic associated with the roomLink sent.
     * @param roomLink roomLink of the room you want statistics of
     * @return an integer between -1, 0 , 1 that represents the statistics of the speed reactions
     */
    @GetMapping("stat/speed/{roomLink}")
    @ResponseBody
    public int getSpeedStat(@PathVariable String roomLink) {

        return reactionService.getSpeedStat(roomLink);

    }


    /**
     * This method return the emotion statistic associated with the roomLink sent.
     * @param roomLink roomLink of the room you want statistics of
     * @return an integer between -1, 0 , 1 that represents the statistics of the emotion reactions
     */
    @GetMapping("stat/emotion/{roomLink}")
    @ResponseBody
    public int getEmotionStat(@PathVariable String roomLink) {

        return reactionService.getEmotionStat(roomLink);

    }

    /**
     * returns the counts of each emotion reaction in the room.
     * @param roomLink roomLink of the room we want reaction information of
     * @return a list of counts where index 0 - confused, 1 - sad, 2 - happy
     */
    @GetMapping("counts/emotion/{roomLink}")
    @ResponseBody
    public List<Integer> getEmotionCounts(@PathVariable String roomLink) {
        return reactionService.getEmotionCounts(roomLink);
    }
}
