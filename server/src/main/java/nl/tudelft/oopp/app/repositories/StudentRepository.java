package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("StudentRepository")

public interface StudentRepository extends JpaRepository<Student, Long> {
}