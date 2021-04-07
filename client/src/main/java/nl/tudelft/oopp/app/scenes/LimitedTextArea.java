package nl.tudelft.oopp.app.scenes;

import javafx.scene.control.TextArea;
import lombok.NoArgsConstructor;
import lombok.Setter;

/****************************************************************************************
*    Title: TextField maxlength
*    Author: Siggouroglou, G
*    Date: 2013
*    Availability: https://stackoverflow.com/questions/15159988/javafx-2-2-textfield-maxlength/15188135#15188135
****************************************************************************************/
@NoArgsConstructor
@Setter
public class LimitedTextArea extends TextArea {
    public int maxlength = 254;

    @Override
    public void replaceText(int start, int end, String text) {
        // Delete or backspace user input.
        if (text.equals("") || getText() == null) {
            super.replaceText(start, end, text);
        } else if (getText().length() < maxlength) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        // Delete or backspace user input.
        if (text.equals("")) {
            super.replaceSelection(text);
        } else if (getText().length() < maxlength) {
            // Add characters, but don't exceed maxlength.
            if (text.length() > maxlength - getText().length()) {
                text = text.substring(0, maxlength - getText().length());
            }
            super.replaceSelection(text);
        }
    }
}
