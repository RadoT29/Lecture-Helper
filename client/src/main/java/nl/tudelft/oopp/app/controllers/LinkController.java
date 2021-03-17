package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

    public void studentCopyLink(){
        StringSelection selection = new StringSelection(linkStudentBox.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    public void moderatorCopyLink(){
        StringSelection selection = new StringSelection(linkModeratorBox.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }
}
