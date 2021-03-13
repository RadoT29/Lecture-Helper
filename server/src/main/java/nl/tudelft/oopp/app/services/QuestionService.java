package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.User;
import nl.tudelft.oopp.app.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class handles all business logic related to the Question object.
 */
@Service
public class QuestionService {

    private QuestionRepository questionRepository;
    private RoomService roomService;
    private UserService userService;

    /**
     * This constructor injects all the dependencies needed by the class.
     * @param questionRepository the Question Repository
     * @param roomService class that handles rooms services
     * @param userService class that handles user services
     */
    @Autowired
    public QuestionService(QuestionRepository questionRepository,
                           RoomService roomService,
                           UserService userService) {
        this.questionRepository = questionRepository;
        this.roomService = roomService;
        this.userService = userService;
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
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



}
