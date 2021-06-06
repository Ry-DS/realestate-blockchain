package com.rmit.realestate.ui;

import com.rmit.realestate.data.Buyer;
import com.rmit.realestate.data.BuyerDao;
import com.rmit.realestate.data.Seller;
import com.rmit.realestate.data.SellerDao;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

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
    ComboBox<Seller> addressProperty;
    @FXML
    Label loanId;

    @FXML
    TableView<Seller> authorityTable;  // For Table

    @FXML
    private TableColumn<Seller, String> addressTable; // For Table

    @FXML
    private TableColumn<Seller, String> ownerTable; // For Table

    ObservableList<Seller> list = FXCollections.observableList(SellerDao.getApproved());

    private final Color error = Color.web("#ff1c1c");

//    ObservableList<String> list = FXCollections.observableArrayList("House 1", "House 2");

    public void initialize() {
        addressProperty.setItems(list);

        authorityTable.setItems(list);
        addressTable.setCellValueFactory(new PropertyValueFactory<>("propertyAddress"));
        ownerTable.setCellValueFactory(new PropertyValueFactory<>("ownerVendorName"));
    }

    @FXML
    public void submit() {
        String fullName1 = fullName.getText();
        String dob1 = dob.getText();
        String currentAddress1 = currentAddress.getText();
        String contactNumber1 = contactNumber.getText();
        String employerName1 = employerName.getText();
        String loanAmount1 = loanAmount.getText();

        Seller seller = addressProperty.getValue();
        String addressProperty1 = seller != null ? this.addressProperty.getValue().getPropertyAddress() : null;

        if (fullName1 == null || fullName1.isBlank()) {
            loanId.setTextFill(error);
            loanId.setText("Fullname not given");
            return;
        }
        if (dob1 == null || dob1.isBlank()) {
            loanId.setTextFill(error);
            loanId.setText("Date of Birth not given");
            return;
        }
        if (currentAddress1 == null || currentAddress1.isBlank()) {
            loanId.setTextFill(error);
            loanId.setText("Current Address not given");
            return;
        }
        if (contactNumber1 == null || contactNumber1.isBlank()) {
            loanId.setTextFill(error);
            loanId.setText("Contact Number not given");
            return;
        }
        if (employerName1 == null || employerName1.isBlank()) {
            loanId.setTextFill(error);
            loanId.setText("Employer Name not given");
            return;
        }
        if (addressProperty1 == null || addressProperty1.isBlank()) {
            loanId.setTextFill(error);
            loanId.setText("Please Select a Property to Buy");
            return;
        }
        if (loanAmount1 == null || loanAmount1.isBlank()) {
            loanId.setTextFill(error);
            loanId.setText("Loan Amount not given");
            return;
        }
        // at this point, all fields are filled, safe to give id.
        loanId.setTextFill(Color.GREEN);
        Buyer buyer = new Buyer(fullName1, dob1, currentAddress1, contactNumber1, employerName1, addressProperty1);
        int id = BuyerDao.addBuyer(buyer);
        loanId.setText("Submitted- " + "Loan Application Id: " + id);

        clear1();

// TODO make buyer object
//        try {
//            Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dob.getText());
//            Buyer buyer = new Buyer(fullName.getText(), date1, currentAddress.getText(), contactNumber.getText(), employerName.getText(),
//                    (Integer) addressProperty.getValue(), loanAmount.getText());
//        }catch (Exception ex){
//
//        }
    }

    public void close(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    public void clear() {
        fullName.clear();
        dob.clear();
        currentAddress.clear();
        contactNumber.clear();
        employerName.clear();
        addressProperty.getSelectionModel().clearSelection();
        loanAmount.clear();
        loanId.setText("");
    }

    public void clear1() {
        fullName.clear();
        dob.clear();
        currentAddress.clear();
        contactNumber.clear();
        employerName.clear();
        addressProperty.getSelectionModel().clearSelection();
        loanAmount.clear();
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
