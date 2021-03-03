package nl.tudelft.oopp.demo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import nl.tudelft.oopp.demo.communication.ServerCommunication;
import nl.tudelft.oopp.demo.data.Quote;

import java.util.List;

public class MainSceneController {

    @FXML
    private TextField search;
    @FXML
    private ListView<Quote> quoteList;

    /**
     * Handles clicking the button.
     */
    public void buttonClicked() {
        List<Quote> result = ServerCommunication.findQuotes(search.getText());
        quoteList.getItems().clear();
        quoteList.getItems().addAll(result);
    }
}
