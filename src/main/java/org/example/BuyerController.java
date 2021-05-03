package org.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;


public class BuyerController {
    @FXML
    TextField fullName;
    @FXML
    TextField dob;
    @FXML
    TextField currentAddress;
    @FXML
    TextField contactNumber;
    @FXML
    TextField employerName;
    @FXML
    TextField loanAmount;
    @FXML
    ComboBox addressProperty;


    public void menuClose(ActionEvent event){
        Platform.exit();
        System.exit(0);
    }

    public void clear(ActionEvent event){
        fullName.clear();
        dob.clear();
        currentAddress.clear();
        contactNumber.clear();
        employerName.clear();
        addressProperty.getSelectionModel().clearSelection();
        loanAmount.clear();
    }

    public void submit(ActionEvent event){

    }

    @FXML
    private void cancel() throws IOException {
        App.setRoot("primary");
    }

}
