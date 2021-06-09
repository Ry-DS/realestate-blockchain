package com.rmit.realestate.ui;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.SecurityEntity;
import com.rmit.realestate.data.Buyer;
import com.rmit.realestate.data.Seller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;


public class BuyerController {
    private final SecurityEntity OWNER = SecurityEntity.BUYER;

    private final Color ERROR_COLOR = Color.web("#ff1c1c");

    @FXML
    TextField fullNameField;
    @FXML
    DatePicker dobField;
    @FXML
    TextField currentAddressField;
    @FXML
    TextField contactNumberField;
    @FXML
    TextField employerNameField;
    @FXML
    TextField loanAmountField;
    @FXML
    ComboBox<Seller> addressPropertyField;
    @FXML
    Label loanIdLabel;

    @FXML
    TableView<Seller> authorityTable;  // For Table

    @FXML
    private TableColumn<Seller, String> addressTable; // For Table

    @FXML
    private TableColumn<Seller, String> ownerTable; // For Table

    @FXML
    public void initialize() {
        addressPropertyField.setItems(App.getSellerDao().getApprovedSellers());
        // support numbers only
        loanAmountField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        authorityTable.setItems(App.getSellerDao().getApprovedSellers());
        addressTable.setCellValueFactory(new PropertyValueFactory<>("propertyAddress"));
        ownerTable.setCellValueFactory(new PropertyValueFactory<>("ownerVendorName"));
    }

    @FXML
    public void submit() {
        int loanAmount = -1;
        LocalDate dob;
        try {
            dob = this.dobField.getConverter().fromString(this.dobField.getEditor().getText());
            if (!loanAmountField.getText().isBlank())
                loanAmount = Integer.parseInt(this.loanAmountField.getText().replaceAll(",", ""));
        } catch (NumberFormatException ex) {
            loanIdLabel.setText("Loan amount isn't a valid number.");
            return;
        } catch (DateTimeParseException ex) {
            loanIdLabel.setText("Given DOB isn't valid.");
            return;
        }
        long dobTime = dob != null ? Date.from(dob.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() : -1;
        String fullName = this.fullNameField.getText();

        String currentAddress = this.currentAddressField.getText();
        String contactNumber = this.contactNumberField.getText();
        String employerName = this.employerNameField.getText();


        Block sellerBlock = App.getBlockchain().findBlockWithData(addressPropertyField.getValue());
        String errMsg = null;

        if (fullName.isBlank()) {
            errMsg = "Full-name not given";
        } else if (dobTime <= 0) {
            errMsg = "DOB cannot be empty.";
        } else if (dobTime > new Date().getTime()) {
            errMsg = "DOB cannot be in the future";
        } else if (currentAddress.isBlank()) {
            errMsg = "Current Address not given";
        } else if (contactNumber.isBlank()) {
            errMsg = "Contact Number not given";
        } else if (employerName.isBlank()) {
            errMsg = "Employer Name not given";
        } else if (sellerBlock == null) {
            errMsg = "Please Select a Property to Buy";
        } else if (loanAmount <= 0) {
            errMsg = "Loan Amount not given";
        } else {
            // at this point, all fields are filled, safe to give id.
            Buyer buyer = new Buyer(fullName, dobTime, currentAddress, contactNumber, employerName, loanAmount, sellerBlock);
            loanIdLabel.setText("Submitting...");
            if (App.getBuyerDao().addBuyer(buyer, OWNER)) {
                loanIdLabel.setTextFill(Color.GREEN);
                loanIdLabel.setText("Submitted - " + "Loan Application Id: " + buyer.getLoanApplicationId());
                clearSubmit();
            } else {
                errMsg = "Failed to verify Buyer onto blockchain.";
            }
        }
        if (errMsg != null) {
            loanIdLabel.setTextFill(ERROR_COLOR);
            loanIdLabel.setText(errMsg);
        }
    }

    public void close(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public void clearButton() {
        clearSubmit();
        loanIdLabel.setText("");
    }

    public void clearSubmit() {
        fullNameField.clear();
        dobField.getEditor().clear();
        currentAddressField.clear();
        contactNumberField.clear();
        employerNameField.clear();
        addressPropertyField.getSelectionModel().clearSelection();
        addressPropertyField.setPromptText("Select a Property");
        loanAmountField.clear();
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
