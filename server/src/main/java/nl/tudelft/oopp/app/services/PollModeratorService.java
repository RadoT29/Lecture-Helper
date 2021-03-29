package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PollModeratorService {

    @Autowired
    private PollRepository pollRepository;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;
    @Autowired
    private PollAnswerRepository pollAnswerRepository;
    @Autowired
    private RoomRepository roomRepository;


}
