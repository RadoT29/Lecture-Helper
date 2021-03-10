package nl.tudelft.oopp.app.repositories;

import nl.tudelft.oopp.app.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {}