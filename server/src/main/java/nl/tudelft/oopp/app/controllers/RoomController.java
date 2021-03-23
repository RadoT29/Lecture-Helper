package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import nl.tudelft.oopp.app.services.RoomService;
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

    @Autowired
    private RoomService roomService;

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
        roomRepository.closeRoom(UUID.fromString(linkId));
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
        Room room = roomRepository.isClose(UUID.fromString(linkId));
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
        System.out.println(linkId);
        Room room = roomRepository.permission(UUID.fromString(linkId));
        return room.getPermission();
    }

    /**
     * PUT Endpoint kick all student.
     *
     * @param linkId - name of the room
     */
    @PutMapping("kickAllStudents/{linkId}")
    @ResponseBody
    public void kickAllStudent(@PathVariable String linkId) {
        roomRepository.kickAllStudents(UUID.fromString(linkId));
    }

    /**
     * This method updates the the number of questions which can be send per time.
     * @param roomLink - the room link after that is found the room id
     * @param numQuestions - the number of questions
     * @param minutes - per minutes
     */
    @PutMapping(path = "/setConstraints/{roomLink}/{numQuestions}/{minutes}")
    @ResponseBody
    public void putConstraints(@PathVariable String roomLink,
                               @PathVariable String numQuestions,
                               @PathVariable String minutes) {
        roomService.putConstraints(
                roomLink, Integer.parseInt(numQuestions),
                Integer.parseInt(minutes)
        );
    }
}
