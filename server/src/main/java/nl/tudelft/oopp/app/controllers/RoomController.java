package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Room;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class RoomController {

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

    @PostMapping("closeRoomByName")
    @ResponseBody
    public void closeRoom(@RequestParam String name){
        //make query and close the room!
    }
}
