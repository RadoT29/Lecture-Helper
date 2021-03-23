package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.IPAddress;
import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Student;
import nl.tudelft.oopp.app.models.User;
import nl.tudelft.oopp.app.repositories.IPAddressRepository;
import nl.tudelft.oopp.app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This class handles all business logic related to the User Object.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IPAddressRepository ipAddressRepository;

    /**
     * Gets all users in the application.
     *
     * @return - a list of all users.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Gets the number of users in the application.
     *
     * @return - their count.
     */
    public long count() {
        return userRepository.count();
    }

    /**
     * Updates a user's name.
     *
     * @param userId   - user's id.
     * @param userName - name of user.
     */
    public void update(long userId, String userName) {
        userRepository.updateUserName(userId, userName);
    }

    /**
     * This method finds a User in the Database from the givenID.
     *
     * @param userID the User to be found
     * @return The User Object with the associated userId
     */
    public User getByID(String userID) {
        Optional<User> dbUser = userRepository.findById(Long.valueOf(userID));
        if (dbUser.isPresent()) {
            return dbUser.get();
        } else {
            System.out.println("User not found");
            return null;
        }
    }

    public void saveStudentIp(String ipAddress, User userId, Room roomLink) {
        IPAddress ipAddressObj = new IPAddress(ipAddress, roomLink, userId);
        ipAddressRepository.save(ipAddressObj);
    }

    public void banUserForThatRoom(String userId, String roomId) {
        ipAddressRepository.banUserForRoom(Long.parseLong(roomId), Long.parseLong(userId));
    }

    public List<Boolean> isUserBanned(String ipAddress, Long roomId) {
        return ipAddressRepository.checkForIpBan(ipAddress, roomId);
    }
}
