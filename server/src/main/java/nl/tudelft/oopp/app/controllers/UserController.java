package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Moderator;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Student;
import nl.tudelft.oopp.app.models.User;
import nl.tudelft.oopp.app.repositories.ModeratorRepository;
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
    private RoomService roomService;
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
            Room room = roomService.getByLink(roomLink);
            if (room.getLinkIdModerator().compareTo(UUID.fromString(roomLink)) == 0) {
                System.out.println("A moderator is created!");
                Moderator moderator = new Moderator(room);
                moderatorRepository.save(moderator);
                return moderator;
            } else if (room.getLinkIdStudent().compareTo(UUID.fromString(roomLink)) == 0) {
                System.out.println("A student is created!");
                Student student = new Student(room);
                studentRepository.save(student);
                return student;
            }
            return null;
        } catch (IllegalArgumentException exception) {
            System.out.println("No room exists");
            return null;
        }
        catch (NullPointerException exception){
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
