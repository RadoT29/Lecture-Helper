package nl.tudelft.oopp.app.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import nl.tudelft.oopp.app.communication.AdminCommunication;
import nl.tudelft.oopp.app.models.AdminSession;


import java.awt.*;
import java.io.IOException;


public class AdminPasswordController {

    @FXML
    private TextField passwordBox;
    @FXML
    private Label invalidPassword;

    private AdminSession session;

    /**
     * Loads scene where you can insert admin password.
     * @param stage The stage where the scene needs to be loaded
     * @throws IOException - if an error occurs during loading
     */
    public static void enterPasswordAdmin(Stage stage) throws IOException {

        Parent loader = new FXMLLoader(AdminPasswordController.class
                .getResource("/adminPasswordScene.fxml")).load();

        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * 0.8;
        double height = screenSize.getHeight() * 0.8;

        Scene scene = new Scene(loader, width, height);

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Check that the given password is the correct admin password.
     * @throws IOException - Thrown if an error occurs while loading next scene
     */
    public void checkPassword() throws IOException {

        String password = passwordBox.getText();

        // If the password is null, tips the user to insert one
        if (password.equals("")) {
            invalidPassword.setText("Insert a password!");
            invalidPassword.setVisible(true);
            return;
        }

        // Sends a request to check that the password is correct
        boolean succeeded = AdminCommunication.checkPassword(password);

        if (succeeded) {
            // Loads next scene and save password for next requests
            session = AdminSession.getInstance(password);
            AdminSceneController.init((Stage) passwordBox.getScene().getWindow());

        } else {
            // Tips the user that the password was wrong
            invalidPassword.setText("Wrong Password");
            invalidPassword.setVisible(true);
        }


    }
}
