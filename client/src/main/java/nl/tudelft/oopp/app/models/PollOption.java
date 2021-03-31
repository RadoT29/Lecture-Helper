package nl.tudelft.oopp.app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor

public class PollOption {
    public long id;
    public String optionText;
    public boolean correct;
    public double scoreRate;
}
