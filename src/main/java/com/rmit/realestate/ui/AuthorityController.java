package com.rmit.realestate.ui;

import com.rmit.realestate.blockchain.SecurityEntity;
import com.rmit.realestate.data.Seller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.io.IOException;

public class AuthorityController {
    private final SecurityEntity OWNER = SecurityEntity.AUTHORITY;

    @FXML
    ComboBox<Seller> addressProperty;
    @FXML
    Label message;

    @FXML
    TableView<Seller> authorityTable;  // For Table

    @FXML
    private TableColumn<Seller, String> addressTable; // For Table

    @FXML
    private TableColumn<Seller, String> ownerTable; // For Table

    @FXML
    private TableColumn<Seller, String> licNumberTable;

    @FXML
    private TableColumn<Seller, String> pid; // For Table

    ObservableList<Seller> list = FXCollections.observableList(App.getSellerDao().getPendingSellers());


    public void initialize() {
        addressProperty.setItems(list);

        authorityTable.setItems(list);
        addressTable.setCellValueFactory(new PropertyValueFactory<>("propertyAddress"));
        ownerTable.setCellValueFactory(new PropertyValueFactory<>("ownerVendorName"));
        licNumberTable.setCellValueFactory(new PropertyValueFactory<>("licenceNumber"));
        pid.setCellValueFactory(new PropertyValueFactory<>("permitId"));

    }

    public void close() {
        System.exit(0);
        Platform.exit();
    }


    public void about() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("Authority - Real Estate");
        about.setContentText("Application Version: 1.0.0.0");
        about.setHeaderText("About Authority - Real Estate");
        about.show();
        about.getGraphic().getClass().getResourceAsStream("icon.png");
    }


    public void cancel() throws IOException {
        App.setRoot("Main");
    }

    public void accept() {
        message.setTextFill(Color.RED);
        Seller seller = addressProperty.getValue();
        String addressProperty = seller != null ? this.addressProperty.getValue().getPropertyAddress() : null;

        if (addressProperty == null || addressProperty.isBlank()) {
            message.setText("Please Select a Property");
        } else {
            message.setText("Submitting...");
            if (App.getSellerDao().approve(seller, OWNER)) {
                message.setTextFill(Color.GREEN);
                message.setText(seller.getOwnerVendorName() + "'s property has been accepted for sale");
                authorityTable.refresh();
                this.addressProperty.getSelectionModel().clearSelection();
            } else {
                message.setText("Failed to verify block on blockchain.");
            }

        }
    }

    public void decline() {
        message.setTextFill(Color.RED);
        Seller seller = addressProperty.getValue();
        String addressProperty = seller != null ? seller.getPropertyAddress() : null;
        if (addressProperty == null || addressProperty.isBlank()) {
            message.setText("Please Select a Property");
            return;
        }
        message.setText("Submitting...");
        if (App.getSellerDao().disapprove(seller, OWNER)) {
            message.setTextFill(Color.GREEN);
            message.setText(seller.getOwnerVendorName() + "'s property has been declined for sale");
            authorityTable.refresh();
            this.addressProperty.getSelectionModel().clearSelection();
        }else{
            message.setText("Failed to verify block on blockchain.");
        }
    }


}
