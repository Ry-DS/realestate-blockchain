package com.rmit.realestate.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    TextField userNameField;
    @FXML
    TextField passwordField;
    @FXML
    Label message;

    @FXML
    private void cancel() throws IOException {
        App.setRoot("Main");
    }

    @FXML
    private void login() throws IOException {
        String username = userNameField.getText();
        String password = passwordField.getText();
        if (username.equals("admin") && password.equals("pass")) {
            App.setRoot("Main");
        } else if (username.equals("seller") && password.equals("pass")) {
            App.setRoot("seller");
        } else if (username.equals("buyer") && password.equals("pass")) {
            App.setRoot("BuyerForm");
        } else if (username.equals("authority") && password.equals("pass")) {
            App.setRoot("authority");
        } else if (username.equals("bank") && password.equals("pass")) {
            App.setRoot("bank");
        } else {
            message.setText("Wrong Info Provided");
        }

        if (username.isBlank()) {
            message.setText("Please Enter Username");
        } else if (password.isBlank()) {
            message.setText("Please Enter Password");
        }

    }

    public void close(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

}
