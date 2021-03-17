package nl.tudelft.oopp.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.tudelft.oopp.app.models.Room;
import nl.tudelft.oopp.app.models.Student;
import nl.tudelft.oopp.app.models.Moderator;
import nl.tudelft.oopp.app.repositories.RoomRepository;
import nl.tudelft.oopp.app.repositories.StudentRepository;
import nl.tudelft.oopp.app.repositories.ModeratorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ModeratorStudentTest {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModeratorRepository moderatorRepository;

    /**
     * Save student to the student repository and then check if it is there.
     */
    @Test
    public void saveAndRetrieveStudent() {
        Room room = new Room("room name");
        roomRepository.save(room);

        Student student = new Student("Radoslav", room);
        studentRepository.save(student);

        Student student2 = studentRepository.getOne(student.getId());
        assertEquals(student, student2);
    }

    /**
     * Save moderator to the moderator repository and then check if it is there.
     */
    @Test
    public void saveAndRetrieveModerator() {
        Room room = new Room("room name");
        roomRepository.save(room);

        Moderator mod = new Moderator("Rado", room);
        moderatorRepository.save(mod);

        Moderator mod2 = moderatorRepository.getOne(mod.getId());
        assertEquals(mod, mod2);
    }

    /**
     * Save room to the room repository and then check if it is there by moderator.
     */
    @Test
    public void saveAndRetrieveRoomViaModerator() {
        Room room = new Room("OurRoom");
        roomRepository.save(room);

        Moderator mod = new Moderator("Pedro", room);
        moderatorRepository.save(mod);

        Moderator moderator = moderatorRepository.getOne(mod.getId());
        assertEquals(moderator.getRoomId(), room);
        assertEquals(moderator.getRoomId().getName(), "OurRoom");
    }

    /**
     * Save room to the room repository and then check if it is there by student.
     */
    @Test
    public void saveAndRetrieveRoomViaStudent() {
        Room room = new Room("room name");
        roomRepository.save(room);

        Student stud = new Student("Pedro", room);
        studentRepository.save(stud);

        Student student = studentRepository.getOne(stud.getId());
        assertEquals(student.getRoomId(), room);
        assertEquals(student.getRoomId().getName(), "room name");
    }


}
