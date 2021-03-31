package nl.tudelft.oopp.app.exceptions;

import javafx.scene.control.Label;

public class InvalidInputException extends Exception {

    public InvalidInputException(Label warningLabel) {
        warningLabel.setVisible(true);
    }
}
