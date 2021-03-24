package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class SpeedReactionTest {

    @Autowired
    private SpeedReactionRepository speedReactionRepository;


    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ModeratorRepository moderatorRepository;

    /**
     * This test saves a Reaction on the database and then retrieves it.
     * to assert that it's the same.
     */
    @Test
    public void saveAndRetrieveSpeedReaction() {

        //A reaction has a room associated to it.
        Room room = new Room("room name");
        roomRepository.save(room);

        //A reaction has a user associated to it
        Student student = new Student("Radoslav", room);
        studentRepository.save(student);

        SpeedReaction speedReactionBeforeDb = new SpeedReaction(1,room, student);
        speedReactionRepository.save(speedReactionBeforeDb);


        SpeedReaction speedReactionAfterDb = speedReactionRepository
                .getOne(speedReactionBeforeDb.getId());

        assertEquals(speedReactionBeforeDb, speedReactionAfterDb);
        assertEquals(speedReactionBeforeDb.getValue(),speedReactionAfterDb.getValue());
    }

    /**
     * This test saves a SpeedReaction on the database and then asserts.
     * that the associated Room is the same one.
     */
    @Test
    public void saveAndRetrieveRoomViaSpeedReaction() {
        //A SpeedReaction has a room associated to it.
        Room room = new Room("room name");
        roomRepository.save(room);

        //A SpeedReaction has a user associated to it
        Student student = new Student("Radoslav", room);
        studentRepository.save(student);

        SpeedReaction speedReactionBeforeDb = new SpeedReaction(-1,room, student);
        speedReactionRepository.save(speedReactionBeforeDb);

        SpeedReaction speedReactionAfterDb = speedReactionRepository
                .getOne(speedReactionBeforeDb.getId());

        assertEquals(speedReactionAfterDb.getRoom(), room);
        assertEquals(speedReactionAfterDb.getRoom().getName(), "room name");

    }

    /**
     * This test saves a SpeedReaction on the database and then asserts.
     * that the associated User is the same one.
     */
    @Test
    public void saveAndRetrieveUserViaSpeedReaction() {
        //A speedReaction has a room associated to it.
        Room room = new Room("OurRoom");
        roomRepository.save(room);

        //A speedReaction has a user associated to it.
        Moderator mod = new Moderator("Natalia", room);
        moderatorRepository.save(mod);

        SpeedReaction speedReactionBeforeDb = new SpeedReaction(0,room, mod);
        speedReactionRepository.save(speedReactionBeforeDb);

        SpeedReaction speedReactionAfterDb = speedReactionRepository
                .getOne(speedReactionBeforeDb.getId());

        assertEquals(speedReactionAfterDb.getUser(), mod);
        assertEquals(speedReactionAfterDb.getUser().getName(), "Natalia");

    }


}
