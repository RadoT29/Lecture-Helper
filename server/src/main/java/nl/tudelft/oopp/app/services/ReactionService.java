package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.repositories.EmotionReactionRepository;
import nl.tudelft.oopp.app.repositories.ReactionRepository;
import nl.tudelft.oopp.app.repositories.SpeedReactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReactionService {

    private ReactionRepository reactionRepository;
    private EmotionReactionRepository emotionReactionRepository;
    private SpeedReactionRepository speedReactionRepository;
    private RoomService roomService;
    private UserService userService;

    /**
     * This constructor injects all the dependencies needed by the class.
     * @param reactionRepository the Reaction Repository
     * @param speedReactionRepository the SpeedReaction Repository
     * @param emotionReactionRepository the EmotionReaction Repository
     * @param roomService class that handles rooms services
     * @param userService class that handles user services
     */
    @Autowired
    public ReactionService(ReactionRepository reactionRepository,
                           EmotionReactionRepository emotionReactionRepository,
                           SpeedReactionRepository speedReactionRepository,
                           RoomService roomService,
                           UserService userService) {

        this.reactionRepository = reactionRepository;
        this.emotionReactionRepository = emotionReactionRepository;
        this.speedReactionRepository = speedReactionRepository;
        this.roomService = roomService;
        this.userService = userService;
    }

    /**
     * This methods saves a reaction on the database with a User and a Room associated to it.
     * If a reaction of the same type already existed, it gets updated to the new value
     * @param roomLink link of the room associated to the reaction
     * @param userIdStr id of the user associated to the reaction
     * @param reaction either a SpeedReaction or a EmotionReaction sent by the user
     */
    public void addNewReaction(String roomLink, String userIdStr, Reaction reaction) {

        User user = userService.getByID(userIdStr); //Finds the user associated with the ID
        reaction.setUser(user);
        Room room = roomService.getByLink(roomLink); // Finds the room associated with the link
        reaction.setRoom(room);

        Long roomId = room.getId();
        Long userId = Long.parseLong(userIdStr);

        // Checks if reaction is of type SpeedReaction
        if (reaction instanceof SpeedReaction) {

            // Looks for a SpeedReaction with the same room and user
            Long id = speedReactionRepository.getReactionIdByRoomAndUserIds(roomId, userId);

            // If reaction is found it gets updated with new value
            if (id != null) {
                speedReactionRepository.updateValue(id, reaction.getValue());

            // If no such reaction is found it gets created
            } else {
                speedReactionRepository.save((SpeedReaction) reaction);
            }

        // Checks if reaction is of type SpeedReaction
        } else if (reaction instanceof EmotionReaction) {
            // Looks for a EmotionReaction with the same room and user
            Long id = emotionReactionRepository.getReactionIdByRoomAndUserIds(roomId, userId);

            // If reaction is found it gets updated with new value
            if (id != null) {
                emotionReactionRepository.updateValue(id, reaction.getValue());

            // If no such reaction is found it gets created
            } else {
                emotionReactionRepository.save((EmotionReaction) reaction);
            }
        }

    }

    /**
     * This method checks the value of the SpeedReactions associated to the room
     * to return average value.
     * @param roomLink link of the room we need statistics of
     * @return an integer between -1, 0, 1 that represents the average value of the reactions
     */
    public int getSpeedStat(String roomLink) {

        Long roomId = roomService.getByLink(roomLink).getId();

        try {
            int speedSum = speedReactionRepository.getSpeedSum(roomId);
            int speedCount = speedReactionRepository.getSpeedCount(roomId);
            return (int) Math.round((double) speedSum / speedCount);

        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * This method checks the value of the EmotionReactions associated to the room
     * to return average value.
     * @param roomLink link of the room we need statistics of
     * @return an integer between -1, 0, 1 that represents the average value of the reactions
     */
    public int getEmotionStat(String roomLink) {

        Long roomId = roomService.getByLink(roomLink).getId();
        try {
            int emotionSum = emotionReactionRepository.getEmotionSum(roomId);
            int emotionCount = emotionReactionRepository.getEmotionCount(roomId);
            return (int) Math.round((double) emotionSum / emotionCount);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * retrieves the count of each reaction in the room and writes it to a list.
     * @param roomLink link of the room we need reaction information of
     * @return a list of counts of each reaction where list index: 0 - confused, 1 - sad, 2 - happy
     */
    public List<Integer> getEmotionCounts(String roomLink) {
        Long roomId = roomService.getByLink(roomLink).getId();
        List<Integer> counts = new ArrayList<>();
        // add confused
        counts.add(emotionReactionRepository.getEmotionCountOfValue(roomId, -1));
        // add sad
        counts.add(emotionReactionRepository.getEmotionCountOfValue(roomId, 0));
        // add happy
        counts.add(emotionReactionRepository.getEmotionCountOfValue(roomId, 1));
        return counts;
    }
}
