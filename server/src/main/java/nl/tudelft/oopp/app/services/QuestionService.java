package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Upvote;
import nl.tudelft.oopp.app.models.User;
import nl.tudelft.oopp.app.repositories.QuestionRepository;
import nl.tudelft.oopp.app.repositories.UpvoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * This class handles all business logic related to the Question object.
 */
@Service
public class QuestionService {

    private QuestionRepository questionRepository;
    private RoomService roomService;
    private UserService userService;
    private UpvoteRepository upvoteRepository;

    /**
     * This constructor injects all the dependencies needed by the class.
     * @param questionRepository the Question Repository
     * @param roomService class that handles rooms services
     * @param userService class that handles user services
     */
    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           RoomService roomService,
                           UserService userService,
                           UpvoteRepository upvoteRepository) {
        this.questionRepository = questionRepository;
        this.roomService = roomService;
        this.userService = userService;
        this.upvoteRepository = upvoteRepository;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    /**
     * gets all questions from the room.
     * @param roomLinkString a room link
     * @return list of questions from the room.
     *      Questions have to roomId and UserId changed to 0.
     */
    public List<Question> getAllQuestionsByRoom(String roomLinkString) {
        UUID roomLink = UUID.fromString(roomLinkString);
        List<Question> result = questionRepository.findAllByRoomLink(roomLink);
        for (Question q : result) {
            q.getRoom().setId(0);
            q.getUser().setId(0);
        }
        return result;
    }


    /**
     * Get the last added question by user that created the request.
     * @param roomLink - the room link
     * @param userId - the users Id
     * @return list of questions from the room.
     *      Questions have to roomId and UserId changed to 0.
     */
    public String getSingleQuestion(String roomLink, String userId) {
        Room room = roomService.getByLink(roomLink);
        User user = userService.getByID(userId);
        return questionRepository.getSingularQuestion(room.getId(), user.getId());

    }



    /**
     * This method gets the correct Room and User associated with the question that has been sent.
     * @param roomLink the roomLink where the question has been asked
     * @param userId the id of the user who asked the question
     * @param question the question that has been asked
     */
    public void addNewQuestion(String roomLink, String userId, Question question) {
        User user = userService.getByID(userId); //Finds the user associated with the ID
        question.setUser(user);
        Room room = roomService.getByLink(roomLink); // Finds the room associated with the link
        question.setRoom(room);
        questionRepository.save(question); //Saves the room on the Database
        System.out.println("Question saved!");
    }


    /**
     * calls the questionRepository to delete the question from the database.
     * calls the upvoteRepository to delete the upVotes related to that question
     * @param questionId long id of the question to be deleted
     **/
    public void dismissQuestion(long questionId) {
        //delete upVvotes
        upvoteRepository.deleteUpVotesByQuestionId(questionId);
        //delete the question
        questionRepository.deleteById(questionId);
    }

    /**
     * Method add an upvote on the server side.
     * @param questionId - Id of the question upvote to be added
     * @param userId - Id of user making the change
     */
    public void addUpvote(String questionId, String userId) {
        long questionId2 = Long.parseLong(questionId);

        User user = userService.getByID(userId);
        Question question = questionRepository.getOne(questionId2);

        Upvote upvote = new Upvote(question, user);
        upvoteRepository.save(upvote);
        upvoteRepository.incrementUpVotes(questionId2);

    }


    /**
     * Method to delete the upvote on the server side.
     * @param questionId - Id of the question upvote to be deleted
     * @param userId - Id of user making the change
     */
    public void deleteUpvote(String questionId, String userId) {
        long questionId2 = Long.parseLong(questionId);
        long userId2 = Long.parseLong(userId);

        System.out.print(questionId2 + " " + userId2);

        Upvote temp = upvoteRepository.findUpvoteByUserAndQuestion(userId2, questionId2);
        upvoteRepository.deleteById(temp.getId());
        upvoteRepository.decrementUpVotes(questionId2);
    }

    /**
     * For now this works but need to set up the room ID
     * part so to identify the room from which to delete.
     */
    public void clearQuestions(String roomLink) {
        Room room = roomService.getByLink(roomLink);

        questionRepository.clearQuestions(room.getId());
    }

}
