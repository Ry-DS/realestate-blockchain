package com.rmit.realestate.ui;

import com.rmit.realestate.data.Buyer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



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
    ComboBox<String> addressProperty;
    @FXML
    Label loanId;

//    @FXML
//    Color Error = new Color(10,10,255);

    ObservableList<String> list = FXCollections.observableArrayList("House 1","House 2");

    public void initialize(){
        addressProperty.setItems(list);
    }

    @FXML
    public void submit(){
        String fullName1 = fullName.getText();
        String dob1 = dob.getText();
//        try {
//            Date dob1 = new SimpleDateFormat("dd/MM/yyyy").parse(dob.getText());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        String currentAddress1 = currentAddress.getText();
        String contactNumber1 = contactNumber.getText();
        String employerName1 = employerName.getText();
        String loanAmount1 = loanAmount.getText();
        String addressProperty1 = (String) addressProperty.getValue();

        if (fullName1 == null || fullName1.isBlank()) {
            loanId.setTextFill(Color.RED);
            loanId.setText("Fullname not given");
            return;
        }
        if (dob1 == null || dob1.isBlank()) {
            loanId.setTextFill(Color.RED);
            loanId.setText("Date of Birth not given");
            return;
        }
        if (currentAddress1 == null || currentAddress1.isBlank()) {
            loanId.setTextFill(Color.RED);
            loanId.setText("Current Address not given");
            return;
        }
        if (contactNumber1 == null || contactNumber1.isBlank()) {
            loanId.setTextFill(Color.RED);
            loanId.setText("Contact Number not given");
            return;
        }
        if (employerName1 == null || employerName1.isBlank()) {
            loanId.setTextFill(Color.RED);
            loanId.setText("Employer Name not given");
            return;
        }
        if (addressProperty1 == null || addressProperty1.isBlank()) {
            loanId.setTextFill(Color.RED);
            loanId.setText("Please Select a Property to Buy");
            return;
        }
        if (loanAmount1 == null || loanAmount1.isBlank()) {
            loanId.setTextFill(Color.RED);
            loanId.setText("Loan Amount not given");
            return;
        }
        if (fullName1 != null && dob1 != null && currentAddress1 != null && contactNumber1 != null && employerName1 != null && loanAmount1 != null && addressProperty1 != null){
            loanId.setTextFill(Color.BLACK);
            loanId.setText("Loan Application Id: " + "00001");
            return;
        }

//        try {
//            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dob.getText());
//            Buyer buyer = new Buyer(fullName.getText(), date1, currentAddress.getText(), contactNumber.getText(), employerName.getText(),
//                    (Integer) addressProperty.getValue(), loanAmount.getText());
//        }catch (Exception ex){
//
//        }
    }

    public void close(ActionEvent event){
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
        loanId.setText("");
    }

    @FXML
    private void cancel() throws IOException {
        App.setRoot("Main");
    }

    @FXML
    private void about() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("Buyer - Bank");
        about.setContentText("Application Version: 1.0.0.0");
        about.setHeaderText("About Buyer - Bank");
        about.show();
        about.getGraphic().getClass().getResourceAsStream("icon.png");
    }

}
