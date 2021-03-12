package nl.tudelft.oopp.app.services;

import nl.tudelft.oopp.app.models.User;
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

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public long count() {
        return userRepository.count();
    }

    /**
     * This method finds a User in the Database from the givenID.
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
}
