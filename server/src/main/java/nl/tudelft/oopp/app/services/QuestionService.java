package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
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
    private UserRepository userRepository;

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
     *
     * @param roomLinkString a room link
     * @return list of questions from the room.
     */
    public List<Question> getAllQuestionsByRoom(String roomLinkString) {
        UUID roomLink = UUID.fromString(roomLinkString);
        
        //Makes sure the duration attribute is set
        List<Long> updateTime = questionRepository.findAllIdByRoomLink(roomLink);
        Room room = roomRepository.findByLink(roomLink);
        getTimeOfQuestions(updateTime, room.getId());
        
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
     *
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
     *
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
        //delete the answer
        answerRepository.deleteByQuestionID(questionId);
        Question question = questionRepository.getOne(questionId);
        //delete the question
        questionRepository.deleteById(questionId);
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
        Integer upvoteValue = 0;
        if (user.getIsModerator()) {
            upvote.setValue(10);
            upvoteValue = 10;
        } else {
            upvote.setValue(1);
            upvoteValue = 1;
        }
        Integer count = questionRepository.getUpVoteCount(questionId2);
        question.setTotalUpVotes(count + upvoteValue);
        questionRepository.save(question);
        upvoteRepository.save(upvote);


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

        Question question = questionRepository.getOne(questionId2);

        Upvote temp = upvoteRepository.findUpvoteByUserAndQuestion(userId2, questionId2);
        Integer upvoteValue = temp.value;

        Integer count = questionRepository.getUpVoteCount(questionId2);
        question.setTotalUpVotes(count - upvoteValue);
        questionRepository.save(question);
        upvoteRepository.deleteById(temp.getId());
    }

    /**
     * For now this works but need to set up the room ID
     * part so to identify the room from which to delete.
     */
    public void clearQuestions(String roomLink) {
        Room room = roomService.getByLinkModerator(roomLink);
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
        } else {
            answerRepository.deleteByQuestionID(questionId2);
        }
        answerRepository.save(answer);
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
     *
     * @param roomLink - the room link
     * @return list of questions from the room.
     */
    public String exportQuestions(String roomLink) {
        Room room = roomService.getByLinkModerator(roomLink);

        return getQuestionsAndAnswers(room);

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
        getTimeOfQuestions(questionIds, room.getId());

        //will be used to store the string time
        String time;

        for (int i = 0; i < questionIds.size(); i++) {
            long questionId = questionIds.get(i);

            Question question = questionRepository.getQuestionById(questionIds.get(i));

            time = question.getDuration();

            log = log + "Question: \n"
                    + time + ": " + questionRepository.getQuestionText(questionId) + "\n";

            List<Long> answerIds = answerRepository.getAllAnswerIds(questionId);
            getTimeOfAnswers(answerIds, room.getId());

            if (answerIds.size() == 0) {
                log = log + "This question was not answered yet\n\n";
            } else {
                log = log + "Answers:\n";
                for (int j = 0; j < answerIds.size(); j++) {
                    long answerId = answerIds.get(j);

                    Answer answer = answerRepository.getAnswerById(answerIds.get(j));

                    time = answer.getDuration();

                    log = log + time + ": " + answerRepository.getAnswerText(answerId) + "\n\n";
                }
            }

        }

        return log;

    }

    /**
     * Method that will make the connection to the database and retrieve the final upVotes.
     *
     * @param questionId - question to retrieve from
     * @param roomLink   - room where request came from
     * @return number of upvotes
     */
    public Integer getUpVotes(long questionId, UUID roomLink) {
        String roomLink2 = roomLink.toString();
        Room room = roomService.getByLink(roomLink2);

        Integer totalUpVotes = upvoteRepository.getUpVotes(questionId, room.getId());
        
        if (totalUpVotes == null) {
            totalUpVotes = 0;
        }

        return totalUpVotes;

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


    /**
     * Method to set the creation time of the questions (after room created)
     * **The user and the client might have different timeZones, that being said
     * firstly we make sure the the user's time is the same as the server's
     * then we will get the time period between the creation of teh room and the creation
     * of the question.
     * @param questions - List of question Ids
     * @param roomId - room selected
     */
    public void getTimeOfQuestions(List<Long> questions, long roomId) {
        

        LocalDateTime roomTime = roomRepository.getRoomTime(roomId);
        

        for (int i = 0; i < questions.size(); i++) {
            Question question = questionRepository.getQuestionById(questions.get(i));

            //Makes sure time is in the correct timeZone
            LocalDateTime questionTime = question.getCreatedAt();

            String duration = formatDuration(roomTime, questionTime);
            questionRepository.updateDuration(question.getId(), duration);
            setAge(questionTime, question);

        }

    }


    /**
     * Method to set the answer time of the answers(after room creation)
     * This method works the same as before but for answers.
     * @param answers - List with ids of questions to be considered
     * @param roomId - id of the room
     */
    public void getTimeOfAnswers(List<Long> answers, long roomId) {

        LocalDateTime roomTime = roomRepository.getRoomTime(roomId);

        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answerRepository.getAnswerById(answers.get(i));
           
            LocalDateTime answerTime = answer.getCreatedAt();

            String duration = formatDuration(roomTime, answerTime);
            
            answer.setDuration(duration);
            answerRepository.updateDuration(answer.getId(), duration);
            
        }

    }

    /**
     * This method formats the duration following the desired patterns "HH:mm:ss".
     * @param roomTime - Time of the room creation
     * @param elementTime - Time of creation from Answer/Question
     * @return String with formatted date
     */
    public String formatDuration(LocalDateTime roomTime, LocalDateTime elementTime) {
        Duration duration = Duration.between(roomTime, elementTime);
        long minutes = duration.toMinutes() - duration.toHours() * 60;
        long seconds = duration.toSeconds() - 60 * minutes;

        return (duration.toHours() + ":" + minutes + ":" + seconds);
    }


    /**
     * Method to update the age of the question
     * It compares the local time with the ZonedTime of the question 
     * (this to make sure they are in the same timeZone).
     * @param actualTime - ZonedTime of the question
     */
    public void setAge(LocalDateTime actualTime, Question question) {
        ZonedDateTime current = ZonedDateTime.now();
        
        Duration age = Duration.between(actualTime, current);
        
        //Age is stored in minutes
        String age1 = age.toSeconds() + "";
        questionRepository.updateAge(question.getId(), age1);
        
        
    }
    
}
