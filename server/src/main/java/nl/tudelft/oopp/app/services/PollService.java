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

    /**
     * Get all the polls for a given room.
     * Calculate and set the options scored rate,
     * that is how much % of the answers were right.
     * @param roomLinkString uuid link for the room
     * @return list of all the polls of the room
     */
    public List<Poll> getPolls(String roomLinkString) {
        UUID roomLink = UUID.fromString(roomLinkString);
        List<Poll> polls = pollRepository.findAllByRoomLink(roomLink);
        for (Poll poll :
                polls) {
            for (PollOption pollOption :
                    poll.getPollOptions()) {

                int answerCount = pollAnswerRepository
                        .countByPollOptionId(pollOption.getId());
                int markedCount = pollAnswerRepository
                        .countMarkedByPollOptionId(pollOption.getId());

                if (answerCount == 0) {
                    pollOption.setScoreRate(-1);

                } else if (pollOption.isCorrect()) {
                    pollOption.setScoreRate((float)markedCount / answerCount);
                } else {
                    pollOption.setScoreRate(1 - (float)markedCount / answerCount);
                }
            }
        }

        return polls;
    }

    /**
     * Check if the link given has moderator access.
     * @param roomLink uuid link of the room
     * @return if the access was granted
     */
    private boolean isModerator(String roomLink) {
        Room room = roomService.getByLinkModerator(roomLink);

        if (room == null) {
            return false;
        } else {
            return true;
        }
    }

    //Moderator services

    /**
     * Create a new empty poll.
     * @param roomLink uuid link of the room
     * @return the new poll's id
     */
    public long createPoll(String roomLink) {
        if (!isModerator(roomLink)) {
            return 0;
        }
        Room room = this.roomService.getByLink(roomLink);
        Poll poll = new Poll(room);
        this.pollRepository.save(poll);
        return poll.getId();
    }

    /**
     * Auxiliary method that removes all the poll's options.
     * @param pollId id of the poll to be cleared
     */
    private void clearPollOptions(long pollId) {
        this.pollOptionRepository.deleteOptionsFromRoom(pollId);
    }


    /**
     * Auxiliary method that adds a new option to a poll.
     * @param pollId id of the poll to add the new option
     * @param optionText the text of the option
     * @param isCorrect whether this option is a correct one
     */
    private void addPollOption(long pollId, String optionText, boolean isCorrect) {
        Poll poll = this.pollRepository.findById(pollId).get();
        PollOption pollOption = new PollOption(poll, optionText, isCorrect);
        this.pollOptionRepository.save(pollOption);
    }

    /**
     * Update the poll with the given data.
     * Delete and replace all options
     * @param roomLink room of the poll
     * @param pollId id of the poll to be updated
     * @param poll new poll
     */
    public void updateAndOpenPoll(String roomLink, long pollId, Poll poll) {
        if (!isModerator(roomLink)) {
            return;
        }
        System.out.println(poll.toString());
        clearPollOptions(pollId);
        for (PollOption pollOption :
                poll.getPollOptions()) {
            System.out.println(pollOption);
            addPollOption(pollId, pollOption.getOptionText(), pollOption.isCorrect());
        }
        this.pollRepository.updateAndOpenPoll(pollId, poll.getQuestion());
    }

    /**
     * Set poll as finished.
     * @param roomLink moderator link of the polls room
     * @param pollId id of the poll
     */
    public void finishPoll(String roomLink, long pollId) {
        if (!isModerator(roomLink)) {
            return;
        }
        this.pollRepository.finishPoll(pollId);
    }

    //Student services
    public List<PollAnswer> getAnswers(long pollId,long userId) {
        return pollAnswerRepository.findAnswersByUserAndPollId(pollId,userId);
    }

    private void updateAnswer(long answerId, boolean isMarked) {
        pollAnswerRepository.updateAnswer(answerId, isMarked);
    }

    /**
     * Create or update an answer for given student/poll.
     * @param userId student who sent the answer
     * @param poll poll with the answers given
     */
    public void createAnswers(long userId, Poll poll) {

        List<PollAnswer> pollAnswers = new ArrayList<>();
        for (PollOption answerReceived :
                poll.getPollOptions()) {
            long pollOptionId = answerReceived.getId();
            boolean isMarked = answerReceived.isCorrect();
            PollAnswer pollAnswer = pollAnswerRepository
                    .findAnswerByUserAndOptionId(pollOptionId, userId);
            if (pollAnswer != null) {
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
