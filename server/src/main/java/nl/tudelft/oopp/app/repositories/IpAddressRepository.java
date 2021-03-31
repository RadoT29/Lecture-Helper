package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.IpAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface IpAddressRepository extends JpaRepository<IpAddress, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE IpAddress ip set ip.access=false where ip.roomId.id=?1 and ip.userId.id=?2")
    void banUserForRoom(long roomId, long userId);

    @Modifying
    @Transactional
    @Query("UPDATE IpAddress ip set ip.access=true where ip.roomId.id=?1")
    void unbanAllUsersForRoom(long roomId);

    @Query(value = "SELECT ip.access from IpAddress ip where ip.ipAddress=?1 and ip.roomId.id=?2")
    List<Boolean> checkForIpBan(String ipAddress, long roomId);

}
