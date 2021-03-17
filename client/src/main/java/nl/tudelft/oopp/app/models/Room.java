package nl.tudelft.oopp.app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor

public class Room {
    public String name;
    public UUID linkIdStudent;
    public UUID linkIdModerator;
    public boolean isOpen;
    public boolean permission;

    public Room(String name) {
        this.name = name;
    }

}
