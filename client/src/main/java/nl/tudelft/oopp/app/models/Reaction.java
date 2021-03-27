package nl.tudelft.oopp.app.models;

public abstract class Reaction {

    public long id;
    public Room room;
    public User user;
    public int value;

    public Reaction(int value) {
        this.value = value;
    }
}
