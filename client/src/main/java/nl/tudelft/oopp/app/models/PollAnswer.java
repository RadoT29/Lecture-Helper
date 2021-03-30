package nl.tudelft.oopp.app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class PollAnswer {
    public long id;
    public Student student;
    public PollOption pollOption;
    public boolean isMarked;
}
