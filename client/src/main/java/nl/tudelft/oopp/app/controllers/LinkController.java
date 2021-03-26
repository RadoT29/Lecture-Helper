package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;


public class LinkController {

    @FXML
    private TextField linkStudentBox;
    @FXML
    private TextField linkModeratorBox;

    /**
     * This method creates a popup containing the links for the room just created.
     * @param studentLink link for a student User
     * @param moderatorLink link for a Moderator User
     * @throws IOException error if popup doesn't load correctly
     */
    public void getLinks(String studentLink, String moderatorLink) throws IOException {


        Parent loader = new FXMLLoader(getClass().getResource("/linkScene.fxml")).load();

        Stage linkStage = new Stage();

        Scene scene = new Scene(loader);

        TextField studentBox = (TextField) scene.lookup("#linkStudentBox");
        studentBox.setText(studentLink);

        TextField moderatorBox = (TextField) scene.lookup("#linkModeratorBox");
        moderatorBox.setText(moderatorLink);

        linkStage.setScene(scene);
        linkStage.show();

    }

    /**
     * This method copies the student link to the clipboard.
     */
    public void studentCopyLink() {
        StringSelection selection = new StringSelection(linkStudentBox.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }


    /**
     * This method copies the moderator link to the clipboard.
     */
    public void moderatorCopyLink() {
        StringSelection selection = new StringSelection(linkModeratorBox.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}
