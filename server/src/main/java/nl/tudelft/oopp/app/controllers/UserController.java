package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Moderator;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Student;
import nl.tudelft.oopp.app.repositories.ModeratorRepository;
import nl.tudelft.oopp.app.repositories.StudentRepository;
import nl.tudelft.oopp.app.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;


@Controller
public class UserController {

    final
        RoomService roomService;
    final
        ModeratorRepository moderatorRepository;
    final
        StudentRepository studentRepository;

    /**
     * User Controller constructor.
     * @param roomService - The Room Service that users the Room Repository.
     * @param moderatorRepository - The Moderator Repository.
     * @param studentRepository - The Student Repository.
     */
    @Autowired
    public UserController(RoomService roomService, ModeratorRepository moderatorRepository,
                          StudentRepository studentRepository) {
        this.roomService = roomService;
        this.moderatorRepository = moderatorRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * GET Endpoint to add a new user.
     **/
    @GetMapping(path = "/room/user/{roomLink}")
    @ResponseBody
    public String roomExists(@PathVariable String roomLink) {
        System.out.println(roomService.count());
        Room room = roomService.getByLink(UUID.fromString(roomLink));

        if (room.getLinkIdModerator().equals(UUID.fromString(roomLink))) {
            System.out.println("A moderator is created!");
            Moderator moderator = new Moderator(room);
            moderatorRepository.save(moderator);
            return "Moderator";
        } else if (room.getLinkIdStudent().equals(UUID.fromString(roomLink))) {
            System.out.println("A student is created!");
            Student student = new Student(room);
            studentRepository.save(student);
            return "Student";
        }
        return null;

    }

}
