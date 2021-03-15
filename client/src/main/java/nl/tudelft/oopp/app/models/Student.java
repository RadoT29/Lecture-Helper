package nl.tudelft.oopp.app.models;

public class Student extends User {

    /**
     * Constructor for Student.
     * @param id - same as user
     * @param name - same as user
     * @param roomId - same as user
     * @param isModerator - same as user
     */
    public Student(long id,String name, Room roomId, boolean isModerator) {
        super(id, name, roomId, isModerator);
    }



}
