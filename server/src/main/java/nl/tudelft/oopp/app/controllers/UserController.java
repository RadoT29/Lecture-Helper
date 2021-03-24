package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.repositories.ModeratorRepository;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import nl.tudelft.oopp.app.repositories.StudentRepository;
import nl.tudelft.oopp.app.services.QuestionService;
import nl.tudelft.oopp.app.services.RoomService;
import nl.tudelft.oopp.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * This class is used to control the following:
 * Adding a user
 * Adding a nickName to an already existing user.
 */
@Controller
public class UserController {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ModeratorRepository moderatorRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;

    /**
     * Create a new user via entered room link.
     *
     * @param roomLink - the link for the room.
     * @return - a User object, which will be saved on the client side.
     */
    @GetMapping(path = "/room/user/{roomLink}")
    @ResponseBody
    public User roomExists(@PathVariable String roomLink) {
        try {
            Room room = roomRepository.findByLink(UUID.fromString(roomLink));
            if (room.getLinkIdModerator().compareTo(UUID.fromString(roomLink)) == 0) {
                Moderator moderator = new Moderator(room);
                moderatorRepository.save(moderator);
                System.out.println("Moderator created @ Room "
                        + moderator.getRoomId().getId() + ":"
                        + "\n\tUser id: " + moderator.getId()
                        + "\n\tUser name: " + moderator.getName());
                return moderator;
            } else if (room.getLinkIdStudent().compareTo(UUID.fromString(roomLink)) == 0) {
                Student student = new Student(room);
                studentRepository.save(student);
                System.out.println("Moderator created @ Room" + student.getRoomId() + ":"
                        + "\n\tUser id: " + student.getId()
                        + "\n\tUser name: " + student.getName());
                return student;
            }
            return null;
        } catch (IllegalArgumentException exception) {
            System.out.println("Invalid room link entered: " + roomLink);
            return null;
        } catch (NullPointerException exception) {
            return null;
        }
    }

    /**
     * Add a nickname to an already existing user.
     *
     * @param nickName - the nickname.
     * @param userId   - the id of the user, used to find the right one.
     */
    @PostMapping(path = "/room/user/{userId}/nick/{nickName}")
    @ResponseBody
    public void setNickName(@PathVariable("nickName") String nickName,
                            @PathVariable("userId") String userId) {
        userService.update(Long.parseLong(userId), nickName);
    }

    /**
     * This method saves the request ip for that room link and the question id.
     * @param userId - the user id
     * @param roomId - the room link
     * @param request - the ip address
     */
    @PostMapping(path = "/room/user/saveIP/{userId}/{roomId}")
    @ResponseBody
    public void saveStudentIp(@PathVariable("userId") String userId,
                              @PathVariable("roomId") String roomId,
                              HttpServletRequest request) {
        System.out.println(request.getRemoteAddr());
        Room room = roomService.getByLink(roomId);
        User user = userService.getByID(userId);
        System.out.println("reach here");
        userService.saveStudentIp(request.getRemoteAddr(), user, room);
    }

    /**
     * This method ban an user by question id, room id, and ip address.
     * @param questionId - the id of the question
     * @param roomLink - the room link
     */
    @PutMapping(path = "/room/user/banUserRoom/{questionId}/{roomLink}")
    @ResponseBody
    public void banUserForThatRoom(@PathVariable("questionId") String questionId,
                                   @PathVariable("roomLink") String roomLink) {
        String roomId = String.valueOf(roomService.getByLink(roomLink).getId());
        String userId = String.valueOf(questionService.findUserByQuestionId(questionId));
        userService.banUserForThatRoom(userId, roomId);
    }

    /**
     * Check if that request ip is banned for that room.
     * @param roomLink - the room link, from where is found the room id
     * @param request - the ip address of the user/request
     * @return - false if the user is banned, else true
     */
    @GetMapping(path = "/room/user/isBanned/{roomLink}")
    @ResponseBody
    public boolean isUserBanned(@PathVariable("roomLink") String roomLink,
                                HttpServletRequest request) {
        System.out.println(roomLink);
        String roomId = String.valueOf(roomService.getByLink(roomLink).getId());
        List<Boolean> list = userService
                .isUserBanned(request.getRemoteAddr(), Long.valueOf(roomId));
        return !list.contains(false);
    }

    /**
     * This method check if the user is in the limits to ask question.
     * How the method works:
     * It first find the room and check if the timeInterval or
     * numberQuestionsInterval are with value Integer.MAX_VALUE,
     * if so true is return. Otherwise a variable with the local time is made.
     * From it is extracted the time interval and is made a request for the
     * Questions created after that time.
     * @param userId - the user id
     * @param roomLink - the room link
     * @return - true if the size of questions is smaller than
     *      the allowed number. Otherwise false;
     */
    @GetMapping(path = "/room/user/canAskQuestion/{userId}/{roomLink}")
    @ResponseBody
    public boolean canAskQuestion(@PathVariable("userId") String userId,
                                  @PathVariable("roomLink") String roomLink) {

        Room room = roomService.getByLink(roomLink);
        if (room.getTimeInterval() == Integer.MAX_VALUE
                || room.getNumberQuestionsInterval() == Integer.MAX_VALUE) {
            return true;
        }
        LocalDateTime time = LocalDateTime.now();
        time = time.minusMinutes(room.getTimeInterval());
        List<Question> questions = questionService
                .questionsByUserIdRoomIdInterval(userId, room.getId(), time);


        return questions.size() < room.getNumberQuestionsInterval();
    }
}
