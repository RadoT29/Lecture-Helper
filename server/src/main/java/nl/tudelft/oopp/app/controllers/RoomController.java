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
     * PUT Endpoint kick all student.
     *
     * @param linkId - name of the room
     */
    @PutMapping("kickAllStudents/{linkId}")
    @ResponseBody
    public void kickAllStudent(@PathVariable String linkId) {
        roomRepository.kickAllStudents(UUID.fromString(linkId));
    }
}
