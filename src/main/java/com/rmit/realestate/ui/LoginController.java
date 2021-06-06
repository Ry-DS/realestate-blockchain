package com.rmit.realestate.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    TextField userName;
    @FXML
    TextField password;
    @FXML
    Label message;

    @FXML
    private void cancel() throws IOException {
        App.setRoot("Main");
    }

    @FXML
    private void login() throws IOException {
        String username = userName.getText();
        String password1 = password.getText();
        if (username.equals("admin") && password1.equals("pass")) {
            App.setRoot("Main");
        } else if (username.equals("seller") && password1.equals("pass")) {
            App.setRoot("seller");
        } else if (username.equals("buyer") && password1.equals("pass")) {
            App.setRoot("BuyerForm");
        } else if (username.equals("authority") && password1.equals("pass")) {
            App.setRoot("authority");
        } else if (username.equals("bank") && password1.equals("pass")) {
            App.setRoot("bank");
        } else {
            message.setText("Wrong Info Provided");
        }
        if (password1.equals("")) {
            message.setText("Please Enter Password");
        }
        if (username.equals("")) {
            message.setText("Please Enter Username");
        }

    }
    public void close(ActionEvent event){
        Platform.exit();
        System.exit(0);
    }

}
