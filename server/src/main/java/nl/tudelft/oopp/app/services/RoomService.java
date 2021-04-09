package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    /**
     * Looking for a room by UUID link.
     * Opens the room when it's time.
     *
     * @param link - the link of the room which is being searched
     * @return the room
     */
    public Room getByLink(String link) {

        Room room = roomRepository.findByLink(UUID.fromString(link));
        updatePermission(room);
        return room;
    }

    /**
     * Looking for a room by UUID moderatorLink.
     * Opens the room when it's time.
     *
     * @param link - the link of the room which is being searched
     * @return the room
     */
    public Room getByLinkModerator(String link) {

        Room room = roomRepository.findByLinkModerator(UUID.fromString(link));
        updatePermission(room);
        return room;
    }

    /**
     * opens the room if it is now closed and it's already past the startDate
     * and the room hasn't been closed yet (endDate is null).
     * @param room Room to be updated
     */
    public void updatePermission(Room room) {
        if (room == null) {
            return;
        }
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
        //room should be open and hasn't been closed yet
        if (!room.isPermission()
                && now.isAfter(room.getStartDate())) {
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
