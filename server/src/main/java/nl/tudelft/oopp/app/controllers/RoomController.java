package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class RoomController {

    private RoomService service;

    @Autowired
    public RoomController(RoomService roomService) {
        this.service = roomService;
    }

    /**
     * GET Endpoint to retrieve a random quote.
     *
     * @return randomly selected {@link Room}.
     */
    @PostMapping("room")
    @ResponseBody
    public Room getNewRoomLinks(@RequestParam String name) {
        return new Room(name);
    }

    /**
     * PUT Endpoint close the room.
     *
     * @param name - name of the room
     */
    @PutMapping("closeRoomByName")
    public void closeRoom(@RequestParam String name) {
        //make query and close the room!
        service.closeRoom(name);
    }

    /**
     * PUT Endpoint kick all student.
     *
     * @param name - name of the room
     */
    @PutMapping("kickAllStudents")
    public void kickAllStudent(@RequestParam String name) {
        service.kickAllStudents(name);
    }
}
