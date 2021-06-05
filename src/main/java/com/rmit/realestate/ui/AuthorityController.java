package com.rmit.realestate.ui;

import com.rmit.realestate.data.Seller;
import com.rmit.realestate.data.SellerDao;
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
import java.util.Collections;
import java.util.List;

public class AuthorityController {

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

    ObservableList<Seller> list = FXCollections.observableList(SellerDao.getSellers());

    public void initialize() {
        addressProperty.setItems(list);

        authorityTable.setItems(list);
        addressTable.setCellValueFactory(new PropertyValueFactory<>("propertyAddress"));
        ownerTable.setCellValueFactory(new PropertyValueFactory<>("ownerVendorName"));

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
        Seller seller = addressProperty.getValue();
        String addressProperty = seller != null ? this.addressProperty.getValue().getPropertyAddress() : null;

        if (addressProperty == null || addressProperty.isBlank()) {
            message.setText("Please Select a Property");
        } else {
            message.setTextFill(Color.GREEN);
            message.setText(addressProperty + " has been accepted");

            addressTable.setText((addressProperty));

            SellerDao.approve(seller);

        }
    }

    public void decline() {
        String addressProperty1 = addressProperty.getValue().getPropertyAddress();
        if (addressProperty1 == null || addressProperty1.isBlank()) {
            message.setText("Please Select a Property");
        }

        if (addressProperty1 != null) {
            message.setTextFill(Color.GREEN);
            message.setText(addressProperty1 + " has been declined");
        }
    }


}
