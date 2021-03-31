package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PollService {

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

    //Moderator services
    public long createPoll(String roomLink) {
        if (!isModerator(roomLink)) return 0;
        Room room = this.roomService.getByLink(roomLink);
        Poll poll = new Poll(room);
        this.pollRepository.save(poll);
        return poll.getId();
    }

    private void clearPollOptions(long pollId) {
        this.pollOptionRepository.deleteOptionsFromRoom(pollId);
    }

    private void addPollOption(long pollId, String optionText, boolean isCorrect) {
        Poll poll = this.pollRepository.findById(pollId).get();
        PollOption pollOption = new PollOption(poll, optionText, isCorrect);
        this.pollOptionRepository.save(pollOption);
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

    public void finishPoll(String roomLink, long pollId) {
        if (!isModerator(roomLink)) return;
        this.pollRepository.finishPoll(pollId);
    }

    //Student services
    public List<PollAnswer> getAnswers(long pollId,long userId) {
        return pollAnswerRepository.findAnswersByUserAndPollId(pollId,userId);
    }

    private void updateAnswer(long answerId, boolean isMarked) {
        pollAnswerRepository.updateAnswer(answerId, isMarked);
    }

    public void createAnswers(long userId, Poll poll) {

        List<PollAnswer> pollAnswers = new ArrayList<>();
        for (PollOption answerReceived :
                poll.getPollOptions()) {
            long pollOptionId = answerReceived.getId();
            boolean isMarked = answerReceived.isCorrect();
            PollAnswer pollAnswer = pollAnswerRepository
                    .findAnswerByUserAndOptionId(pollOptionId, userId);
            if (pollAnswer!=null) {
                updateAnswer(pollAnswer.getId(), isMarked);
            } else {
                Student student = (Student) userService.getByID(userId + "");
                PollOption pollOption = pollOptionRepository.findById(pollOptionId).get();
                pollAnswer = new PollAnswer(student, pollOption, isMarked);
                pollAnswers.add(pollAnswer);
            }
        }
        pollAnswerRepository.saveAll(pollAnswers);
    }
}
