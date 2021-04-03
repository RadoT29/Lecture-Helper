package nl.tudelft.oopp.app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor

public class Poll {
    public long id;
    public Room room;
    public List<PollOption> pollOptions;
    public String question;
    public boolean open;
    public boolean finished;
}