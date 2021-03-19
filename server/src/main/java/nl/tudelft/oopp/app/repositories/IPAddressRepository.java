package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.IPAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface IPAddressRepository extends JpaRepository<IPAddress, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE IPAddress ip set ip.access=false where ip.roomId.id=?1 and ip.userId.id=?2")
    public void banUserForRoom(long roomId, long userId);


}
