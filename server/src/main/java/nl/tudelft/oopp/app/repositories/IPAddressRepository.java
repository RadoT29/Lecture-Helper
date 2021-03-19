package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.IPAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPAddressRepository extends JpaRepository<IPAddress, Long> {
}
