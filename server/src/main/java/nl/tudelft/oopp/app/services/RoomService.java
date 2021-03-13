package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public long count() {
        return roomRepository.count();
    }

    public Room getByLink(String link) {
        UUID toCheck = UUID.fromString(link);
        return  roomRepository.findByLink(UUID.fromString(link));
    }
}
