package com.rmit.realestate.ui;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
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
    Label lenBlockchainLabel;
    ChangeListener<Number> lenBlockchainListener;
    @FXML
    Label numClientsLabel;
    ChangeListener<Number> numClientsListener;

    @FXML
    Label numServerConLabel;
    ChangeListener<Number> numServerConListener;

    public void initialize() {
        App.blockchainSizeProperty().addListener(lenBlockchainListener = (observable, oldValue, newValue) -> {
            lenBlockchainLabel.setText(String.valueOf(newValue.intValue()));
        });
        App.getPeerConnectionManager().numberOfServerConnectionsProperty().addListener(
                numServerConListener = (observable, oldValue, newValue) -> numServerConLabel.setText(String.valueOf(newValue.intValue())));
        App.getPeerConnectionManager().numOfClientConnectionsProperty().addListener(
                numClientsListener = (observable, oldValue, newValue) -> numClientsLabel.setText(String.valueOf(newValue.intValue())));
        lenBlockchainLabel.setText(String.valueOf(App.blockchainSizeProperty().intValue()));
        numClientsLabel.setText(String.valueOf(App.getPeerConnectionManager().numOfClientConnectionsProperty().intValue()));
        numServerConLabel.setText(String.valueOf(App.getPeerConnectionManager().numberOfServerConnectionsProperty().intValue()));
    }


    public void shutdown() {
        App.blockchainSizeProperty().removeListener(lenBlockchainListener);
        App.getPeerConnectionManager().numOfClientConnectionsProperty().removeListener(numClientsListener);
        App.getPeerConnectionManager().numberOfServerConnectionsProperty().removeListener(numServerConListener);
    }

    public void cancel() throws IOException {
        App.setRoot("Main");
    }

    @FXML
    private void login() throws IOException {
        String username = userNameField.getText();
        String password = passwordField.getText();
        if (App.isAdmin() && username.equals("admin") && password.equals("pass")) {
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
            if (!App.isAdmin() && username.equals("admin") && password.equals("pass")){
                message.setText("Only Admins can login using the provided details");
            } else
            message.setText("Wrong Info Provided");
        }

        if (username.isBlank()) {
            message.setText("Please Enter Username");
        } else if (password.isBlank()) {
            message.setText("Please Enter Password");
        }

    }

    public void close() {
        Platform.exit();
    }

}
