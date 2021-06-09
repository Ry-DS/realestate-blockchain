package com.rmit.realestate.ui;

import com.rmit.realestate.blockchain.SecurityEntity;
import com.rmit.realestate.data.Buyer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
        addressProperty.setItems(App.getBuyerDao().getPendingBuyers());

        bankTable.setItems(App.getBuyerDao().getPendingBuyers());
        fullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        DOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
        // TODO possibly add propertyAddress
        currentAddress.setCellValueFactory(new PropertyValueFactory<>("currentAddress"));
        contactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        employerName.setCellValueFactory(new PropertyValueFactory<>("employerName"));
        selectedProperty.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        loanAmount.setCellValueFactory(new PropertyValueFactory<>("loanAmount"));
        lid.setCellValueFactory(new PropertyValueFactory<>("loanApplicationId"));
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
        message.setTextFill(Color.RED);
        if (buyer == null) {
            message.setText("Please Select a Property");
        } else {
            if (App.getBuyerDao().approve(buyer, OWNER)) {
                message.setTextFill(Color.GREEN);
                message.setText(buyer.getFullName() + "'s loan has been accepted");
                this.addressProperty.getSelectionModel().clearSelection();
            } else message.setText("There was an issue verifying the block onto the blockchain.");
        }
    }

    public void decline() {
        message.setTextFill(Color.RED);
        Buyer buyer = addressProperty.getValue();

        if (buyer == null) {
            message.setText("Please Select a Property");
            return;
        }
        if (App.getBuyerDao().disapprove(buyer, OWNER)) {
            message.setTextFill(Color.GREEN);
            message.setText(buyer.getFullName() + "'s loan has been declined");
            bankTable.refresh();
            this.addressProperty.getSelectionModel().clearSelection();
        } else message.setText("There was an issue verifying the block onto the blockchain.");

    }
}
