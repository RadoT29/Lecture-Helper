package nl.tudelft.oopp.app;

import nl.tudelft.oopp.app.models.*;
import nl.tudelft.oopp.app.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class EmotionReactionTest {

    @Autowired
    private EmotionReactionRepository emotionReactionRepository;


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
    public void saveAndRetrieveEmotionReaction() {

        //A reaction has a room associated to it.
        Room room = new Room("room name");
        roomRepository.save(room);

        //A reaction has a user associated to it
        Student student = new Student("Radoslav", room);
        studentRepository.save(student);

        EmotionReaction emotionReactionBeforeDb = new EmotionReaction(1,room, student);
        emotionReactionRepository.save(emotionReactionBeforeDb);


        EmotionReaction emotionReactionAfterDb = emotionReactionRepository
                .getOne(emotionReactionBeforeDb.getId());
        assertEquals(emotionReactionBeforeDb, emotionReactionAfterDb);
        assertEquals(emotionReactionBeforeDb.getValue(),emotionReactionAfterDb.getValue());
    }

    /**
     * This test saves a EmotionReaction on the database and then asserts.
     * that the associated Room is the same one.
     */
    @Test
    public void saveAndRetrieveRoomViaEmotionReaction() {
        //A EmotionReaction has a room associated to it.
        Room room = new Room("room name");
        roomRepository.save(room);

        //A EmotionReaction has a user associated to it
        Student student = new Student("Radoslav", room);
        studentRepository.save(student);

        EmotionReaction emotionReactionBeforeDb = new EmotionReaction(-1,room, student);
        emotionReactionRepository.save(emotionReactionBeforeDb);

        EmotionReaction emotionReactionAfterDb = emotionReactionRepository
                .getOne(emotionReactionBeforeDb.getId());
        assertEquals(emotionReactionAfterDb.getRoom(), room);
        assertEquals(emotionReactionAfterDb.getRoom().getName(), "room name");

    }

    /**
     * This test saves a EmotionReaction on the database and then asserts.
     * that the associated User is the same one.
     */
    @Test
    public void saveAndRetrieveUserViaEmotionReaction() {
        //A emotionReaction has a room associated to it.
        Room room = new Room("OurRoom");
        roomRepository.save(room);

        //A emotionReaction has a user associated to it.
        Moderator mod = new Moderator("Natalia", room);
        moderatorRepository.save(mod);

        EmotionReaction emotionReactionBeforeDb = new EmotionReaction(0,room, mod);
        emotionReactionRepository.save(emotionReactionBeforeDb);

        EmotionReaction emotionReactionAfterDb = emotionReactionRepository
                .getOne(emotionReactionBeforeDb.getId());
        assertEquals(emotionReactionAfterDb.getUser(), mod);
        assertEquals(emotionReactionAfterDb.getUser().getName(), "Natalia");

    }

    /**
     * test if the repository returns correct count of the emotion reaction
     * of a specific value for a specific room.
     */
    @Test
    public void getEmotionCountOfValueTest() {
        Room room = new Room("The room");
        roomRepository.save(room);

        //A emotionReaction has a user associated to it.
        Moderator mod = new Moderator("Natalia", room);
        moderatorRepository.save(mod);

        EmotionReaction emotionReactionBeforeDb = new EmotionReaction(0,room, mod);
        emotionReactionRepository.save(emotionReactionBeforeDb);

        assertEquals(1, emotionReactionRepository
                .getEmotionCountOfValue(room.getId(), emotionReactionBeforeDb.getValue()));

    }


}
