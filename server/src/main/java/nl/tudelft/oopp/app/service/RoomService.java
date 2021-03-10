package nl.tudelft.oopp.app.service;

import nl.tudelft.oopp.app.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    //@Autowired
    private RoomRepository repository;

    @Autowired
    public RoomService(@Qualifier("RoomRepository") RoomRepository roomRepository) {
        this.repository = roomRepository;
    }

    public void closeRoom(String name) {
        repository.closeRoom(name);
    }

    public void kickAllStudents(String name) {
        repository.kickAllStudents(name);
    }
}
