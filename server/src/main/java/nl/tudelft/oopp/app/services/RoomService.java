package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
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
     * Opens the room when it's time.
     *
     * @param link - the link of the room which is being searched
     * @return the room
     */
    public Room getByLink(String link) {
        UUID toCheck = UUID.fromString(link);
        Room room = roomRepository.findByLink(UUID.fromString(link));
        updatePermision(room);
        return room;
    }

    /**
     * opens the room if it is now closed and it's already past the startDate
     * and the room hasn't been closed yet (endDate is null).
     * @param room Room to be updated
     */
    public void updatePermision(Room room) {
        if (room == null) {
            return;
        }
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        //room should be open and hasn't been closed yet
        if (!room.getPermission()
                && now.isAfter(room.getStartDate())
                && room.getEndDateForStudents() == null) {
            //update in the database (permission=true)
            roomRepository.openRoomForStudents(room.getId());
            //update object
            room.setPermission(true);
        }
    }

    /**
     * saves a scheduled room in the database with the correct startingDate.
     * @param name String name of the room to be saved
     * @param startDateUtcString String representation of the startDate of the Room in UTC)
     * @return newly created room
     */
    public Room scheduleRoom(String name, String startDateUtcString) {
        LocalDateTime startDateUtc = LocalDateTime.parse(startDateUtcString);
        Room room = new Room(name, startDateUtc);
        roomRepository.save(room);
        return room;
    }

    public void putConstraints(String roomLink, int numQuestions, int minutes) {
        roomRepository.putConstraints(UUID.fromString(roomLink), numQuestions, minutes);
    }
}
