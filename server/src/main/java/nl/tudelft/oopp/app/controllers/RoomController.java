package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import nl.tudelft.oopp.app.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.LocalDateTime;
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
     * GET Endpoint to retrieve a random quote.
     *
     * @return randomly selected {@link Room}.
     */
    @PostMapping("scheduleRoom")
    @ResponseBody
    public Room getScheduledRoomLinks(@RequestParam String name,
                                      @RequestBody String startDateUtcString) {
        startDateUtcString = startDateUtcString.substring(1, startDateUtcString.length() - 1);
        return roomService.scheduleRoom(name, startDateUtcString);
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
        roomRepository.closeRoom(UUID.fromString(linkId), LocalDateTime.now(Clock.systemUTC()));
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
        return roomRepository.permission(UUID.fromString(linkId)).getPermission();
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
