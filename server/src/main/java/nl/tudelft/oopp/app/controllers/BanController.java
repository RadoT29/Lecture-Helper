package nl.tudelft.oopp.app.controllers;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.User;
import nl.tudelft.oopp.app.services.QuestionService;
import nl.tudelft.oopp.app.services.RoomService;
import nl.tudelft.oopp.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class BanController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    /**
     * This method saves the request ip for that room link and the question id.
     * @param userId - the user id
     * @param roomId - the room link
     * @param request - the ip address
     */
    @PostMapping(path = "/room/user/saveIP/{userId}/{roomId}")
    @ResponseBody
    public void saveStudentIp(@PathVariable("userId") String userId,
                              @PathVariable("roomId") String roomId,
                              HttpServletRequest request) {
        System.out.println(request.getRemoteAddr());


        Room room = roomService.getByLink(roomId);
        User user = userService.getByID(userId);
        System.out.println("reach here");
        userService.saveStudentIp(request.getRemoteAddr(), user, room);
    }

    /**
     * This method ban an user by question id, room id, and ip address.
     * @param questionId - the id of the question
     * @param roomLink - the room link
     */
    @PutMapping(path = "/room/user/banUserRoom/{questionId}/{roomLink}")
    @ResponseBody
    public void banUserForThatRoom(@PathVariable("questionId") String questionId,
                                   @PathVariable("roomLink") String roomLink) {
        String roomId = String.valueOf(roomService.getByLink(roomLink).getId());
        String userId = String.valueOf(questionService.findUserByQuestionId(questionId));
        userService.banUserForThatRoom(userId, roomId);
    }

    /**
     * Check if that request ip is banned for that room.
     * @param roomLink - the room link, from where is found the room id
     * @param request - the ip address of the user/request
     * @return - false if the user is banned, else true
     */
    @GetMapping(path = "/room/user/isBanned/{roomLink}")
    @ResponseBody
    public boolean isUserBanned(@PathVariable("roomLink") String roomLink,
                                HttpServletRequest request) {
        System.out.println(roomLink);
        String roomId = String.valueOf(roomService.getByLink(roomLink).getId());
        List<Integer> list = userService
                .isUserBanned(request.getRemoteAddr(), Long.valueOf(roomId));
        return list.contains(-1);
    }

    /**
     * This method ban an user by question id, room id, and ip address.
     * @param questionId - the id of the question
     * @param roomLink - the room link
     */
    @PutMapping(path = "/room/user/warnUserRoom/{questionId}/{roomLink}")
    @ResponseBody
    public void warnUserForThatRoom(@PathVariable("questionId") String questionId,
                                   @PathVariable("roomLink") String roomLink) {
        String roomId = String.valueOf(roomService.getByLink(roomLink).getId());
        String userId = String.valueOf(questionService.findUserByQuestionId(questionId));
        userService.warnUserForThatRoom(userId, roomId);
    }

    /**
     * Check if that request ip is banned for that room.
     * @param roomLink - the room link, from where is found the room id
     * @param request - the ip address of the user/request
     * @return - false if the user is banned, else true
     */
    @GetMapping(path = "/room/user/isWarned/{roomLink}")
    @ResponseBody
    public boolean isUserWarned(@PathVariable("roomLink") String roomLink,
                                HttpServletRequest request) {
        System.out.println(roomLink);
        String roomId = String.valueOf(roomService.getByLink(roomLink).getId());
        List<Integer> list = userService
                .isUserWarned(request.getRemoteAddr(), Long.valueOf(roomId));
        return list.contains(0);
    }
}
