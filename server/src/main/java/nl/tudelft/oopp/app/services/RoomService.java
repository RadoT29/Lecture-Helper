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

    /**
     * Looking for a room by UUID link.
     *
     * @param link - the link of the room which is being searched
     * @return the room
     */
    public Room getByLink(String link) {
        UUID toCheck = UUID.fromString(link);
        return  roomRepository.findByLink(UUID.fromString(link));
    }

    public void putConstraints(String roomLink, int numQuestions, int minutes){
        roomRepository.putConstraints(UUID.fromString(roomLink),numQuestions,minutes);
    }
}
