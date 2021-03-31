package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.Feedback;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private FeedbackRepository feedbackRepository;
    private RoomService roomService;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository, RoomService roomService) {
        this.feedbackRepository = feedbackRepository;
        this.roomService = roomService;
    }

    /**
     * associates feedback with a room and
     * adds it feedback to the database.
     * @param roomLink String link of the rated room
     * @param feedback Feedback to be saved
     */
    public void addFeedback(String roomLink, Feedback feedback) {
        //is student link??
        Room room = roomService.getByLink(roomLink);
        feedback.setRoom(room);
        feedbackRepository.save(feedback);
        System.out.print("Feedback added: "
                + "\n\tRoom id: " + room.getId()
                + "\n\tRating: " + feedback.getRating()
                + "\n\tComment: " + feedback.getComment() + "\n");
    }


    /**
     * finds all feedback for the specified room.
     * @param roomLink String link of the room
     * @return List of Feedback
     */
    public List<Feedback> getFeedback(String roomLink) {
        //is moderator link
        Room room = roomService.getByLink(roomLink);
        return feedbackRepository.findAllByRoomId(room.getId());
    }
}
