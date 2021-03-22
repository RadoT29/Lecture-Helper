package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository("UserRepository")

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * This method sets the name of a user.
     * @param id - the user id, used to find the right one.
     * @param name - the name, which will be updated.
     */
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.name=?2 WHERE u.id=?1")
    void updateUserName(long id,String name);
}
