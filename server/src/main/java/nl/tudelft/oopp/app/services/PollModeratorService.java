package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.Poll;
import nl.tudelft.oopp.app.models.PollOption;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PollModeratorService {

    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private PollOptionRepository pollOptionRepository;
    @Autowired
    private PollAnswerRepository pollAnswerRepository;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoomRepository roomRepository;

    private boolean isModerator(String roomLink) {
        Room room = roomService.getByLink(roomLink);
        if (room == null) return false;
        if (room.getLinkIdModerator().equals(UUID.fromString(roomLink))) return true;
        return false;
    }

    public List<Poll> getPolls(String roomLinkString) {
        if (!isModerator(roomLinkString)) return null;
        UUID roomLink = UUID.fromString(roomLinkString);
        return pollRepository.findAllByRoomLink(roomLink);
    }

    public long createPoll(String roomLink) {
        if (!isModerator(roomLink)) return 0;
        Room room = this.roomService.getByLink(roomLink);
        Poll poll = new Poll(room);
        this.pollRepository.save(poll);
        return poll.getId();
    }

    public void updateAndOpenPoll(String roomLink, long pollId, Poll poll) {
        if (!isModerator(roomLink)) return;
        System.out.println(poll.toString());
        clearPollOptions(pollId);
        for (PollOption pollOption :
                poll.getPollOptions()) {
            System.out.println(pollOption);
            addPollOption(pollId, pollOption.getOptionText(), pollOption.isCorrect());
        }
        this.pollRepository.updateAndOpenPoll(pollId, poll.getQuestion());
    }

    public void closePoll(String roomLink, long pollId) {
        if (!isModerator(roomLink)) return;
        this.pollRepository.closePoll(pollId);
    }

    private void addPollOption(long pollId, String optionText, boolean isCorrect) {
        Poll poll = this.pollRepository.findById(pollId).get();
        PollOption pollOption = new PollOption(poll, optionText, isCorrect);
        this.pollOptionRepository.save(pollOption);
    }

    private void clearPollOptions(long pollId) {
        this.pollOptionRepository.deleteOptionsFromRoom(pollId);
    }
}
