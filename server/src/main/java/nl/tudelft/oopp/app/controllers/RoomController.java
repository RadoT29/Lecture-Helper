package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Moderator;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.ModeratorRepository;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class RoomController {

    private RoomRepository roomRepository;
    private ModeratorRepository moderatorRepository;

    /**
     * User Controller constructor.
     * @param roomRepository - The Room Repository.
     * @param moderatorRepository - The Moderator Repository.
     */
    @Autowired
    public RoomController(RoomRepository roomRepository, ModeratorRepository moderatorRepository) {
        this.roomRepository = roomRepository;
        this.moderatorRepository = moderatorRepository;
    }


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

        Moderator lecturer = new Moderator(room);
        moderatorRepository.save(lecturer);

        return room;
    }
}
