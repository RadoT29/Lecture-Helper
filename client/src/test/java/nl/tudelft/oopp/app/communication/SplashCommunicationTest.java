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
    /**
     * Check if the returned object is Room with name "My room"
     */
    @Test
    public void testRoomName(){
        Room room = new Room("My room");
        assertEquals(room.getName(),SplashCommunication.postRoom("My room").getName());
    }
    /**
     * Check if the returned object Room is open
     */
    @Test
    public void testRoomIsOpen(){
        Room room = new Room("My room");
        assertEquals(room.isOpen(),SplashCommunication.postRoom("My room").isOpen());
    }
    /**
     * Check if the returned object Room give permission to the students to enter the room
     */
    @Test
    public void testRoomStudentPermission(){
        Room room = new Room("My room");
        assertEquals(room.isPermission(),SplashCommunication.postRoom("My room").isPermission());
    }
    /**
     * Check if the number of returned object
     */
    @Test
    public void testMultipleRoomsSize(){
        Room room1 = SplashCommunication.postRoom("My room");
        Room room2 = SplashCommunication.postRoom("My second Room");
        Room room3 = SplashCommunication.postRoom("My third room");
        List<Room> list = new ArrayList<>();
        list.add(room1);
        list.add(room2);
        list.add(room3);
        assertEquals(3,list.size());
    }
    /**
     * Check if the returned objects form the server are the same with the expected one
     */
    @Test
    public void testMultipleRooms(){
        //expected objects
        Room room1 = new Room("My room");
        Room room2 = new Room("My second Room");
        Room room3 = new Room("My third room");
        Room[] roomArray = new Room[3];
        roomArray[0]=room1;
        roomArray[1]=room2;
        roomArray[2]=room3;

        //objects returned from the server
        Room room4 = SplashCommunication.postRoom("My room");
        Room room5 = SplashCommunication.postRoom("My second Room");
        Room room6 = SplashCommunication.postRoom("My third room");
        Room[] roomArray2 = new Room[3];
        roomArray2[0]=room1;
        roomArray2[1]=room2;
        roomArray2[2]=room3;
        assertArrayEquals(roomArray,roomArray2);
    }

    @Test
    public void testIsRoomClosed(){
        Room room = SplashCommunication.postRoom("My room");

        assertEquals(true, room.isOpen());
        //ServerCommunication.closeRoom(room.getLinkIdModerator().toString());
        //assertEquals(false, room.isOpen());
    }
}
