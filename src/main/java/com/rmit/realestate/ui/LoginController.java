package com.rmit.realestate.ui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    TextField userName;
    @FXML
    TextField password;

    @FXML
    private void cancel() throws IOException {
        App.setRoot("Main");
    }

    @FXML
    private void login(){

    }

    public void close(ActionEvent event){
        Platform.exit();
        System.exit(0);
    }

}
