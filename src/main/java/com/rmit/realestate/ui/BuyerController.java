package com.rmit.realestate.ui;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.data.Buyer;
import com.rmit.realestate.data.BuyerDao;
import com.rmit.realestate.data.Seller;
import com.rmit.realestate.data.SellerDao;
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
import java.time.format.DateTimeParseException;


public class BuyerController {
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
        addressPropertyField.setItems(SellerDao.getApprovedSellers());
        // support numbers only
        loanAmountField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));
        authorityTable.setItems(SellerDao.getApprovedSellers());
        addressTable.setCellValueFactory(new PropertyValueFactory<>("propertyAddress"));
        ownerTable.setCellValueFactory(new PropertyValueFactory<>("ownerVendorName"));
    }

    @FXML
    public void submit() {
        int loanAmount = -1;
        // assume we're giving an error
        loanIdLabel.setTextFill(ERROR_COLOR);
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
        String fullName = this.fullNameField.getText();

        String currentAddress = this.currentAddressField.getText();
        String contactNumber = this.contactNumberField.getText();
        String employerName = this.employerNameField.getText();


        Block sellerBlock= App.blockchain.findBlockWithData(addressPropertyField.getValue());

        if (fullName.isBlank()) {
            loanIdLabel.setText("Full-name not given");
            return;
        }
        if (currentAddress.isBlank()) {
            loanIdLabel.setText("Current Address not given");
            return;
        }
        if (contactNumber.isBlank()) {
            loanIdLabel.setText("Contact Number not given");
            return;
        }
        if (employerName.isBlank()) {
            loanIdLabel.setText("Employer Name not given");
            return;
        }
        if (sellerBlock == null) {
            loanIdLabel.setText("Please Select a Property to Buy");
            return;
        }
        if (loanAmount <= 0) {

            loanIdLabel.setText("Loan Amount not given");
            return;
        }
        // at this point, all fields are filled, safe to give id.
        loanIdLabel.setTextFill(Color.GREEN);
        Buyer buyer = new Buyer(fullName, dob.toEpochDay(), currentAddress, contactNumber, employerName, loanAmount, sellerBlock);
        int id = BuyerDao.addBuyer(buyer);
        loanIdLabel.setText("Submitted- " + "Loan Application Id: " + id);

        clearSubmit();

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
