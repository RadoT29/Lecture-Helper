package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * This class handles all the Endpoints related to the room class.
 */
@Controller
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;

    /**
     * GET Endpoint to retrieve a random quote.
     *
     * @return randomly selected {@link Room}.
     */
    @PostMapping("room")
    @ResponseBody
    public Room getNewRoomLinks(@RequestParam String name) {
        Room room = new Room(name);
        roomRepository.save(room);
        System.out.println("Room created:" +
                            "\n\tRoom id: " + room.getId() +
                            "\n\tRoom name: " + room.getName() +
                            "\n\tStudent link:" + room.getLinkIdStudent() +
                            "\n\tModerator link:" + room.getLinkIdModerator());
        return room;
    }

    /**
     * PUT Endpoint close the room.
     *
     * @param linkId - name of the room
     */
    //@RequestMapping("")
    @PutMapping("closeRoomById/{linkId}")
    @ResponseBody
    public void closeRoom(@PathVariable String linkId) {
        //make query and close the room!
        Room room = roomRepository.findByLink(UUID.fromString(linkId));
        if (room.getLinkIdModerator().toString().equals(linkId)){
            roomRepository.closeRoom(room.getId());
            System.out.println("Room " + room.getId() + "(name: " + room.getName() + ") was closed for students");
        }
        else {
            System.out.println("Someone tried to close room " + room.getId() + "(name: " + room.getName() + ") with a student link");

        }

    }

    /**
     * Get end point. Receive a request for if the room is open
     *
     * @param linkId - the link of the room for which isClose status is requested
     * @return - true if the room is still open, otherwise false
     */
    @GetMapping("isOpenById/{linkId}")
    @ResponseBody
    public boolean isClose(@PathVariable String linkId) {
        Room room = roomRepository.findByLink(UUID.fromString(linkId));
        return room.getIsOpen();
    }

    /**
     * Get end point. Receive a request for if the students have permission to the room
     *
     * @param linkId - the link of the room for which permission status is requested
     * @return - true if the students have permission to the room, otherwise false
     */
    @GetMapping("hasStudentPermission/{linkId}")
    @ResponseBody
    public boolean hasStudentPermission(@PathVariable String linkId) {
        return roomRepository.findByLink(UUID.fromString(linkId)).getPermission();
    }

    /**
     * PUT Endpoint kick all student.
     *
     * @param linkId - name of the room
     */
    @PutMapping("kickAllStudents/{linkId}")
    @ResponseBody
    public void kickAllStudent(@PathVariable String linkId) {
        Room room = roomRepository.findByLink(UUID.fromString(linkId));
        if (room.getLinkIdModerator().toString().equals(linkId)) {
            roomRepository.kickAllStudents(room.getId());
            System.out.println("Room " + room.getId() + "(name: " + room.getName() + ") had all students kicked out");
        }
    }
}
