package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.app.communication.HomeSceneCommunication;
import nl.tudelft.oopp.app.communication.ServerCommunication;
import nl.tudelft.oopp.app.models.Question;
import nl.tudelft.oopp.app.models.Session;

import java.io.IOException;

/**
 * This class contains the code that is run when the IO objects in the Home page are utilized.
 */
public class HomeSceneController {

    Session session = Session.getInstance();

    @FXML
    private TextField questionInput;

    /**
     * Pressing the sendButton will send all the text in the questionInput
     * to the sever as a Question object.
     */
    public void sendQuestion() {
        Question question = new Question(questionInput.getText());
        HomeSceneCommunication.postQuestion(question);
        questionInput.clear(); // clears question input box
    }

    /**
     * close the room.
     */
    public void closeRoom() {
        Session session = Session.getInstance();
        String linkId = session.getRoomLink();
        ServerCommunication.closeRoom(linkId);
    }

    /**
     * kick all students.
     */
    public void kickAllStudents() {
        Session session = Session.getInstance();
        String linkId = session.getRoomLink();
        ServerCommunication.kickAllStudents(linkId);
    }
}