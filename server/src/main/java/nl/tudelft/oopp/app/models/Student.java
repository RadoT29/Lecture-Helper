package nl.tudelft.oopp.app.models;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This class represents a Student in the application.
 * The student is a type of user and is stored in the database.
 */
@Entity
@Table(name = "students")
public class Student extends User {

    /**
     * Create a student.
     *
     * @param name   - name of student
     * @param roomId - the room, which the student enters.
     */
    public Student(String name, Room roomId) {
        super(name, roomId);
    }

    /**
     * Empty constructor
     * (Intellij insists that this has to be created)
     */
    public Student() {

    }

    @Override
    public boolean equals(Object other) {
        if (super.equals(other)
                && other instanceof Student) {
            return true;
        }

        return false;
    }
}
