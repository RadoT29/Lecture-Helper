package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.repositories.AnswerRepository;
import nl.tudelft.oopp.app.repositories.QuestionRepository;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import nl.tudelft.oopp.app.repositories.UpvoteRepository;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
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
    private AnswerRepository answerRepository;
    private RoomRepository roomRepository;

    /**
     * This constructor injects all the dependencies needed by the class.
     *
     * @param questionRepository the Question Repository
     * @param roomService        class that handles rooms services
     * @param userService        class that handles user services
     */
    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           RoomService roomService,
                           UserService userService,
                           UpvoteRepository upvoteRepository,
                           AnswerRepository answerRepository,
                           RoomRepository roomRepository) {
        this.questionRepository = questionRepository;
        this.roomService = roomService;
        this.userService = userService;
        this.upvoteRepository = upvoteRepository;
        this.answerRepository = answerRepository;
        this.roomRepository = roomRepository;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    /**
     * Gets all questions from the room.
     * gets all questions from the room.
     * Questions have the roomId and UserId changed to 0.
     * @param roomLinkString a room link
     * @return list of questions from the room.
     *
     */
    public List<Question> getAllQuestionsByRoom(String roomLinkString) {
        UUID roomLink = UUID.fromString(roomLinkString);
        //the result list only gets the unanswered questions
        List<Question> result = questionRepository.findAllByRoomLink(roomLink);
        for (Question q : result) {
            q.getRoom().setId(0);
            q.getUser().setId(0);
        }
        return result;
    }


    /**
     * Get the last added question by user that created the request.
     * Questions have to roomId and UserId changed to 0.
     * @param roomLink - the room link
     * @param userId   - the users Id
     * @return list of questions from the room.
     *
     */
    public String getSingleQuestion(String roomLink, String userId) {
        Room room = roomService.getByLink(roomLink);
        User user = userService.getByID(userId);
        return questionRepository.getSingularQuestion(room.getId(), user.getId());

    }



    /**
     * This method sets the correct Room and User associated with the question that has been sent.
     * @param roomLink the roomLink where the question has been asked
     * @param userId   the id of the user who asked the question
     * @param question the question that has been asked
     */
    public void addNewQuestion(String roomLink, String userId, Question question) {
        User user = userService.getByID(userId); //Finds the user associated with the ID
        question.setUser(user);
        Room room = roomService.getByLink(roomLink); // Finds the room associated with the link
        question.setRoom(room);
        questionRepository.save(question); //Saves the room on the Database
        System.out.println("Question created: "
                + "\n\tQuestion id: " + question.getId()
                + "\n\tRoom id: " + question.getRoom().getId()
                + "\n\tUser id: " + question.getUser().getId()
                + "\n\tQuestion: " + question.getQuestionText());
    }


    /**
     * calls the questionRepository to delete the question from the database.
     * calls the upvoteRepository to delete the upVotes related to that question
     *
     * @param questionId long id of the question to be deleted
     **/
    public void dismissQuestion(long questionId) {
        //delete upVotes
        upvoteRepository.deleteUpVotesByQuestionId(questionId);
        //delete answer
        answerRepository.deleteByQuestionID(questionId);
        //delete the question
        questionRepository.deleteById(questionId);
        Question question = questionRepository.getOne(questionId);
        System.out.println("Question " + question.getId() + "(room: "
                + question.getRoom().getName() + ") was deleted by a moderator");
    }

    /**
     * In this method the server itself will check if the question was made by the
     * user that sent the request (thus 2 parameters), only if so the question will
     * be deleted.
     * calls questionRepository to execute DELETE query
     *
     * @param questionId - Id of the question to delete
     * @param userId     - Id of the student attempting to delete
     */
    public void dismissSingular(long questionId, long userId) {
        Question question = questionRepository.getOne(questionId);
        //delete question
        questionRepository.deleteSingular(questionId, userId);
        System.out.println("Question " + question.getId()
                + "(room: " + question.getRoom().getName() + ") was deleted by creator");
    }


    /**
     * Method add an upvote on the server side.
     *
     * @param questionId - Id of the question upvote to be added
     * @param userId     - Id of user making the change
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
     *
     * @param questionId - Id of the question upvote to be deleted
     * @param userId     - Id of user making the change
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
        List<Question> qs = questionRepository.findAllByRoomLink(room.getLinkIdModerator());
        for (Question q : qs) {
            answerRepository.deleteByQuestionID(q.getId());
        }
        questionRepository.clearQuestions(room.getId());
        System.out.println("All questions from room " + room.getId()
                + "(name: " + room.getName() + ") were deleted");
    }

    public Long findUserByQuestionId(String questionId) {
        return questionRepository.getUserByQuestionId(Long.valueOf(questionId)).getId();
    }

    public List<Question> questionsByUserIdRoomIdInterval(
            String userId,
            long roomId,
            LocalDateTime time) {
        return questionRepository
                .questionsByUserIdRoomIdInterval(Long.parseLong(userId), roomId, time);
    }


    /**
     * Calls questionRepository to edit the text of a question.
     *
     * @param questionId long the id of the question to be modified
     * @param newText    String new question text
     */
    public void editQuestionText(long questionId, String newText) {
        questionRepository.editQuestionText(questionId, newText);
    }


    /**
     * Method add an answer on the server side.
     * Once an answer is added we also set the question as answered
     *
     * @param questionId - Id of the question upvote to be added
     * @param userId     - Id of user making the change
     * @param answerText - answer text
     * @param answerType - type of answer given
     */
    public void setAnswered(String answerText,
                            String questionId,
                            String userId,
                            boolean answerType) {
        long questionId2 = Long.parseLong(questionId);

        Moderator user = (Moderator) userService.getByID(userId);
        Question question = questionRepository.getOne(questionId2);

        Answer answer = new Answer(answerText, question, user, answerType);

        if (answerType) {
            questionRepository.updateAnsweredStatus(question.getId(), true);
            answerRepository.save(answer);
        } else {
            answerRepository.deleteByQuestionID(questionId2);
            answerRepository.save(answer);
        }
        questionRepository.setAnswer(questionId2, answerText);

    }


    /**
     * Method to check if a question has been set as answered.
     *
     * @param questionId - question to be checked
     * @return boolean true if yes false if not
     */
    public boolean checkAnswered(String questionId, String roomLink) {
        long questionId2 = Long.parseLong(questionId);
        Room room = roomService.getByLink(roomLink);

        return questionRepository.checkAnswered(questionId2, room.getId());

    }

    /**
     * Get a String with all the questions and their answers (formatted).
     * Questions have to roomId and UserId changed to 0.
     * @param roomLink - the room link
     * @return list of questions from the room.
     *
     */
    public String exportQuestions(String roomLink) {
        Room room = roomService.getByLink(roomLink);

        String logTemp =  getQuestionsAndAnswers(room);
        return logTemp;

    }


    /**
     * Method where all the questions ans answers are retrieved from the database.
     *
     * @param room - room to retrieve questions from
     */
    public String getQuestionsAndAnswers(Room room) {
        List<Long> questionIds = questionRepository.getAllQuestionIds(room.getId());
        String roomName = roomRepository.getRoomName(room.getId());
        String id = String.valueOf(room.getId());
        String log = "\nQuestions and Answers from Room: " + roomName + " " + id + "\n\n";

        //will be used to store the string time
        String time;

        for (int i = 0; i < questionIds.size(); i++) {
            long questionId = questionIds.get(i);

            LocalDateTime tempDate = questionRepository.getQuestionTime(questionId);
            time = getTimeString(tempDate);

            log = log + "Question: \n"
                    + time + ": " + questionRepository.getQuestionText(questionId) + "\n";

            List<Long> answerIds = answerRepository.getAllAnswerIds(questionId);

            if (answerIds.size() == 0) {
                log = log + "This question was not answered yet\n\n";
            } else {
                log = log + "Answers:\n";
                for (int j = 0; j < answerIds.size(); j++) {
                    long answerId = answerIds.get(j);

                    tempDate = answerRepository.getAnswerTime(answerId);
                    time = getTimeString(tempDate);


                    log = log + time + ": " + answerRepository.getAnswerText(answerId) + "\n\n";
                }
            }

        }

        return log;

    }

    /**
     * Transform the dates onto a string.
     * @param tempDate - date to transform
     * @return - a string with the date
     */
    public String getTimeString(LocalDateTime tempDate) {
        //Formatter to transform the dates into string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm.ss");

        LocalTime tempTime = tempDate.toLocalTime();
        String time = tempTime.format(formatter);
        return time;
    }

    /**
     * Method that will make the connection to the database and retrieve the final upVotes.
     * @param questionId - question to retrieve from
     * @param roomLink - room where request came from
     * @return number of upvotes
     */
    public int getModUpVotes(String questionId, String roomLink) {
        long questionId2 = Long.parseLong(questionId);
        Room room = roomService.getByLink(roomLink);
        long roomId = room.getId();
       

        List<Long> totalUpVotes = upvoteRepository.getModUpVotes(questionId2, roomId);
        return totalUpVotes.size();

    }

    public Question findByQuestionId(long questionId) {
        return questionRepository.findById(questionId).get();
    }

    /**
     * Gets all answered questions.
     * @param roomLinkString - the room link.
     * @return - a list of questions.
     */
    public List<Question> getAllAnsweredQuestions(String roomLinkString) {
        UUID roomLink = UUID.fromString(roomLinkString);
        List<Question> result = questionRepository.findAllAnsweredByRoomLink(roomLink);
        for (Question q : result) {
            q.getRoom().setId(0);
            q.getUser().setId(0);
        }

        return result;
    }

}
