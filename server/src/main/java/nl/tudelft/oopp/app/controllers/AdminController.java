package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.services.AdminService;
import nl.tudelft.oopp.app.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("admin")
public class AdminController {

    private AdminService adminService;
    private RoomService roomService;

    @Autowired
    public AdminController(AdminService adminService, RoomService roomService) {
        this.adminService = adminService;
        this.roomService = roomService;
    }

    /**
     * GET Endpoint to check that the admin password is correct.
     * @param password password that needs to be checked
     * @return True if the password is correct, false otherwise
     */
    @GetMapping("/password/{password}")
    @ResponseBody
    public Boolean checkPassword(@PathVariable String password) {
        return adminService.checkAdminPassword(password);
    }

    /**
     * GET endpoint to get all the rooms saved on database.
     * @param password admin password to verify the user making the request
     * @return A List containing all the rooms saved on the database
     */
    @GetMapping("/rooms/{password}")
    @ResponseBody
    public List<Room> getRooms(@PathVariable String password) {

        // Check if the user has the credentials to execute method, if not return
        if (!adminService.checkAdminPassword(password)) {
            return null;
        }

        return adminService.getRooms();
    }

    /**
     * PUT endpoint to unban all users in the given room.
     * @param roomLink - the room link to identify the room that needs unbanning
     */
    @PutMapping(path = "/rooms/unbanUsers/{password}/{roomLink}")
    @ResponseBody
    public void unbanAllUsersForThatRoom(@PathVariable String password,
                                         @PathVariable("roomLink") String roomLink) {

        // Check if the user has the credentials to execute method, if not return
        if (!adminService.checkAdminPassword(password)) {
            return;
        }

        String roomId = String.valueOf(roomService.getByLink(roomLink).getId());
        adminService.unbanAllUsersForRoom(roomId);
    }
}
