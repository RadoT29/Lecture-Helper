package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.repositories.IpAddressRepository;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    // The Admin password
    private final String realPassword = "123";


    private RoomRepository roomRepository;
    private IpAddressRepository ipAddressRepository;

    @Autowired
    public AdminService(RoomRepository roomRepository, IpAddressRepository ipAddressRepository) {
        this.roomRepository = roomRepository;
        this.ipAddressRepository = ipAddressRepository;
    }

    /**
     * Checks the given admin password to see if it's correct.
     * @param password the password that needs checking
     * @return True if it's the same, False otherwise
     */
    public Boolean checkAdminPassword(String password) {

        return realPassword.equals(password);

    }

    /**
     * Returns all the rooms saved on the database.
     * @return A list of all the rooms on the database
     */
    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    /**
     * Unbans all users in the specified room.
     * @param roomId The id of the room where the unbanning needs to happen
     */
    public void unbanAllUsersForRoom(String roomId) {
        ipAddressRepository.unbanAllUsersForRoom(Long.parseLong(roomId));
    }
}
