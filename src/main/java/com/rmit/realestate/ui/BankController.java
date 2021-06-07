package com.rmit.realestate.ui;

import com.rmit.realestate.blockchain.SecurityEntity;
import com.rmit.realestate.data.Buyer;
import com.rmit.realestate.data.BuyerDao;
import com.rmit.realestate.data.Seller;
import com.rmit.realestate.data.SellerDao;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.io.IOException;

public class BankController {
    private final SecurityEntity OWNER = SecurityEntity.BANK;
    @FXML
    ComboBox<Buyer> addressProperty;
    @FXML
    Label message;

    @FXML
    private TableView<Buyer> bankTable;

    @FXML
    private TableColumn<Buyer, String> fullName;

    @FXML
    private TableColumn<Buyer, String> DOB;

    @FXML
    private TableColumn<Buyer, String> currentAddress;

    @FXML
    private TableColumn<Buyer, String> contactNumber;

    @FXML
    private TableColumn<Buyer, String> employerName;

    @FXML
    private TableColumn<Buyer, String> selectedProperty;

    @FXML
    private TableColumn<Buyer, Integer> loanAmount;

    @FXML
    private TableColumn<Buyer, Integer> lid;

    public void initialize() {
        addressProperty.setItems(App.getBuyerDao().getBuyers());

        bankTable.setItems(App.getBuyerDao().getBuyers());
        fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        DOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        currentAddress.setCellValueFactory(new PropertyValueFactory<>("currentAddress"));
        contactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        employerName.setCellValueFactory(new PropertyValueFactory<>("employerName"));
        selectedProperty.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        loanAmount.setCellValueFactory(new PropertyValueFactory<>("loanAmount"));
        lid.setCellValueFactory(new PropertyValueFactory<>("")); //TODO ADD Loan ID to Table when Buyer Submits The form For each ID Generated Display Next to Correct name
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

    public void accept() {
        Buyer buyer = addressProperty.getValue();

        if (buyer == null) {
            message.setTextFill(Color.RED);
            message.setText("Please Select a Property");
        } else {
            message.setTextFill(Color.GREEN);
            message.setText(buyer.getFullName() + "'s loan has been accepted");
            bankTable.refresh();
            this.addressProperty.getSelectionModel().clearSelection();
            App.getBuyerDao().approve(buyer, OWNER);
        }
    }

    public void decline() {
        Buyer buyer = addressProperty.getValue();

        if (buyer == null) {
            message.setTextFill(Color.RED);
            message.setText("Please Select a Property");
            return;
        }

        message.setTextFill(Color.GREEN);
        message.setText(buyer.getFullName() + "'s loan has been declined");
        bankTable.refresh();
        this.addressProperty.getSelectionModel().clearSelection();
        App.getBuyerDao().disapprove(buyer, OWNER);

    }
}
