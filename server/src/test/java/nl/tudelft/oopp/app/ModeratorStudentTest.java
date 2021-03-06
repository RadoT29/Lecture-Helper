package nl.tudelft.oopp.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Student;
import nl.tudelft.oopp.app.models.Moderator;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import nl.tudelft.oopp.app.repositories.StudentRepository;
import nl.tudelft.oopp.app.repositories.ModeratorRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ModeratorStudentTest {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModeratorRepository moderatorRepository;


    @Test
    @Order(1)
    public void saveAndRetrieveStudent() {
        Room room = new Room("room name");
        roomRepository.save(room);

        Student student = new Student("Radoslav",room);
        studentRepository.save(student);

        Student student2 = studentRepository.getOne((long) 1);
        assertEquals(student, student2);
    }

    @Test
    @Order(2)
    public void saveAndRetrieveModerator() {
        Room room = new Room("room name");
        roomRepository.save(room);

        Moderator mod = new Moderator("Rado",room);
        moderatorRepository.save(mod);

        Moderator mod2 = moderatorRepository.getOne((long) 2);
        assertEquals(mod, mod2);
        assertEquals(mod.getRoomId().getId(), 2);
    }

    @Test
    @Order(3)
    public void saveAndRetrieveRoomViaModerator() {
        Room room = new Room("OurRoom");
        roomRepository.save(room);

        Moderator mod = new Moderator("Pedro",room);
        moderatorRepository.save(mod);

        Moderator moderator = moderatorRepository.getOne((long) 3);
        assertEquals(moderator.getRoomId(), room);
        assertEquals(moderator.getRoomId().getName(), "OurRoom");
    }

    @Test
    @Order(4)
    public void saveAndRetrieveRoomViaStudent() {
        Room room = new Room("room name");
        roomRepository.save(room);

        Student stud = new Student("Pedro",room);
        studentRepository.save(stud);

        Student student = studentRepository.getOne((long) 4);
        assertEquals(student.getRoomId(), room);
        assertEquals(student.getRoomId().getName(), "room name");
    }


}
