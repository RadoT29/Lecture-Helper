package nl.tudelft.oopp.app.communication;

import nl.tudelft.oopp.app.models.Room;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SplashCommunicationTest {

    /**
     * Check if the returned object form the server is not null
     */
    @Test
    public void testRandomRoom() {
        assertNotNull(SplashCommunication.postRoom("My room"));
    }

}
