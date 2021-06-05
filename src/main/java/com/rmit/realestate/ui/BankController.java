package com.rmit.realestate.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class BankController {

    @FXML
    ComboBox<String> addressProperty;
    @FXML
    Label message;

    ObservableList<String> list = FXCollections.observableArrayList("Name 1","Name 2");

    public void initialize(){
        addressProperty.setItems(list);
    }

    public void close() {
        System.exit(0);
        Platform.exit();
    }


    public void about() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("Administrator - Bank");
        about.setContentText("Application Version: 1.0.0.0");
        about.setHeaderText("About Administrator - Bank");
        about.show();
        about.getGraphic().getClass().getResourceAsStream("icon.png");
    }


    public void cancel() throws IOException {
        App.setRoot("Main");
    }

    public void accept(){
        String addressProperty1 = (String) addressProperty.getValue();
        if (addressProperty1 == null || addressProperty1.isBlank()){
            message.setText("Please Select a Property");
        }

        if (addressProperty1 != null){
            message.setTextFill(Color.GREEN);
            message.setText(addressProperty1 + " has been accepted");
        }
    }

    public void decline(){
        String addressProperty1 = (String) addressProperty.getValue();
        if (addressProperty1 == null || addressProperty1.isBlank()){
            message.setText("Please Select a Property");
        }

        if (addressProperty1 != null){
            message.setTextFill(Color.GREEN);
            message.setText(addressProperty1 + " has been declined");
        }
    }
}
