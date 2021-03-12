package nl.tudelft.oopp.app.communication;

import nl.tudelft.oopp.app.models.Room;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class SplashCommunicationTest {

    @Test
    public void testRandomRoom() {
        assertNotNull(ServerCommunication.postRoom("My room"));
    }

    @Test
    public void testRoomName(){
        Room room = new Room("My room");
        assertEquals(room.getName(),ServerCommunication.postRoom("My room").getName());
    }
    @Test
    public void testRoomIsOpen(){
        Room room = new Room("My room");
        assertEquals(room.isOpen(),ServerCommunication.postRoom("My room").isOpen());
    }
    @Test
    public void testRoomStudentPermission(){
        Room room = new Room("My room");
        assertEquals(room.isPermission(),ServerCommunication.postRoom("My room").isPermission());
    }

    @Test
    public void testMultipleRoomsSize(){
        Room room1 = ServerCommunication.postRoom("My room");
        Room room2 = ServerCommunication.postRoom("My second Room");
        Room room3 = ServerCommunication.postRoom("My third room");
        List<Room> list = new ArrayList<>();
        list.add(room1);
        list.add(room2);
        list.add(room3);
        assertEquals(3,list.size());
    }
    @Test
    public void testMultipleRooms(){
        Room room1 = new Room("My room");
        Room room2 = new Room("My second Room");
        Room room3 = new Room("My third room");
        Room[] roomArray = new Room[3];
        roomArray[0]=room1;
        roomArray[1]=room2;
        roomArray[2]=room3;

        Room room4 = ServerCommunication.postRoom("My room");
        Room room5 = ServerCommunication.postRoom("My second Room");
        Room room6 = ServerCommunication.postRoom("My third room");
        Room[] roomArray2 = new Room[3];
        roomArray2[0]=room1;
        roomArray2[1]=room2;
        roomArray2[2]=room3;
        assertArrayEquals(roomArray,roomArray2);
    }
}
