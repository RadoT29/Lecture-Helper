package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Moderator;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Student;
import nl.tudelft.oopp.app.models.User;
import nl.tudelft.oopp.app.repositories.ModeratorRepository;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import nl.tudelft.oopp.app.repositories.StudentRepository;
import nl.tudelft.oopp.app.services.RoomService;
import nl.tudelft.oopp.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
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
    private ModeratorRepository moderatorRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserService userService;

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
                System.out.println("Moderator created @ Room" + moderator.getRoomId().getId() + ":" +
                        "\n\tUser id: " + moderator.getId() +
                        "\n\tUser name: " + moderator.getName());
                return moderator;
            } else if (room.getLinkIdStudent().compareTo(UUID.fromString(roomLink)) == 0) {
                Student student = new Student(room);
                studentRepository.save(student);
                System.out.println("Moderator created @ Room" + student.getRoomId() + ":" +
                        "\n\tUser id: " + student.getId() +
                        "\n\tUser name: " + student.getName());
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

}
