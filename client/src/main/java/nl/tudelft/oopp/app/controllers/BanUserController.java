package nl.tudelft.oopp.app.controllers;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.Window;
import nl.tudelft.oopp.app.communication.BanCommunication;
import nl.tudelft.oopp.app.models.Session;

public class BanUserController {
    private Session session = Session.getInstance();

    void initialize() {
    }

    private String questionId;

    void initData(String questionId) {
        this.questionId = questionId;
    }

    public void banUser(ActionEvent event) {
        //Node question = questionCell.getParent();
        BanCommunication.banUserForThatRoom(questionId, session.getRoomLink());
        close(event);
    }

    public void close(ActionEvent event) {

        Window window = ((Node) (event.getSource())).getScene().getWindow();
        if (window instanceof Stage) {
            ((Stage) window).close();
        }
    }
}
