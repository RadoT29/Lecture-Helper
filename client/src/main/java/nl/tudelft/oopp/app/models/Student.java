package nl.tudelft.oopp.app.models;

public class Student extends User {

    /**
     * Constructor for Student.
     * @param id - same as user
     * @param name - same as user
     * @param roomLink - same as user
     * @param isModerator - same as user
     */
    public Student(String id,String name, String roomLink, boolean isModerator) {
        super(id, name, roomLink, isModerator);
    }




}
