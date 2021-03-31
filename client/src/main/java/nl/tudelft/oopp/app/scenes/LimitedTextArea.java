package nl.tudelft.oopp.app.scenes;

import javafx.scene.control.TextArea;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class LimitedTextArea extends TextArea {
    public int limit = 254;

    @Override
    public void replaceText(int start, int end, String text) {
        super.replaceText(start, end, text);
        verify();
    }

    @Override
    public void replaceSelection(String text) {
        super.replaceSelection(text);
        verify();
    }

    /**
     * makes sure that the text in the textarea is not longer than the limit.
     */
    public void verify() {
        if (getText().length() > limit) {
            setText(getText().substring(0, limit));
        }
    }
}
