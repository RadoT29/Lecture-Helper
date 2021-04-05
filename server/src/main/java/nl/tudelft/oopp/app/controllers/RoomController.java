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
        System.out.println("Room created:"
                            + "\n\tRoom id: " + room.getId()
                            + "\n\tRoom name: " + room.getName()
                            + "\n\tStudent link:" + room.getLinkIdStudent()
                            + "\n\tModerator link:" + room.getLinkIdModerator());
        return room;
    }


    @GetMapping("room/name/{roomLink}")
    @ResponseBody
    public String getRoomName(@PathVariable String roomLink) {
        return roomService.getByLink(roomLink).getName();
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
     * Get end point. Receive a request for if the students have permission to the room
     *
     * @param linkId - the link of the room for which permission status is requested
     * @return - true if the students have permission to the room, otherwise false
     */
    @GetMapping("hasStudentPermission/{linkId}")
    @ResponseBody
    public boolean isOpen(@PathVariable String linkId) {
        Room room = roomService.getByLink(linkId);
        //System.out.println("The room is open: " + room.isOpen());
        return room.isOpen() && room.isPermission();
    }

    /**
     * PUT Endpoint kick all student.
     *
     * @param linkId - name of the room
     */
    @PutMapping("closeRoomForStudents/{linkId}")
    @ResponseBody
    public void kickAllStudent(@PathVariable String linkId) {
        Room room = roomRepository.findByLink(UUID.fromString(linkId));
        if (room.getLinkIdModerator().toString().equals(linkId)) {
            roomRepository.closeRoomStudents(room.getId());
            System.out.println("Room " + room.getId()
                    + "(name: " + room.getName() + ") had all students kicked out");
        }
    }

    /**
     * PUT Endpoint kick all student.
     *
     * @param linkId - name of the room
     */
    @PutMapping("openRoomForStudents/{linkId}")
    @ResponseBody
    public void openRoomStudent(@PathVariable String linkId) {
        Room room = roomRepository.findByLink(UUID.fromString(linkId));
        if (room.getLinkIdModerator().toString().equals(linkId)) {
            roomRepository.openRoomForStudents(room.getId());
            System.out.println("Room " + room.getId()
                    + "(name: " + room.getName() + ") had all students kicked out");
        }
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
