package com.rmit.realestate.ui;

import com.rmit.realestate.data.Seller;
import com.rmit.realestate.data.SellerDao;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SellerController {
    @FXML
    TextField property_address_field;
    @FXML
    TextField owner_vendor_name_field;
    @FXML
    Button building_design_button;
    @FXML
    TextField license_number_field;
    @FXML
    Label permitId;
    @FXML
    File selectedFile;

    public void submit() {

        String propertyAddress = property_address_field.getText();
        String owner = owner_vendor_name_field.getText();
        String license = license_number_field.getText();

        if (propertyAddress == null || propertyAddress.isBlank()) {
            permitId.setTextFill(Color.RED);
            permitId.setText("Address not given");
            return;
        }
        if (owner == null || owner.isBlank()) {
            permitId.setTextFill(Color.RED);
            permitId.setText("Owner/Vendor not given");
            return;
        }
        if (license == null || license.isBlank()) {
            permitId.setTextFill(Color.RED);
            permitId.setText("Licence Number not given");
            return;
        }
        if (selectedFile == null) {
            permitId.setTextFill(Color.RED);
            permitId.setText("No File Selected");
            return;
        }
        // successful
        permitId.setTextFill(Color.BLACK);
        Seller seller=new Seller(propertyAddress, owner, selectedFile);
        int id = SellerDao.addSeller(seller);
        permitId.setText("Permit Application Id: " + id);

    }

    public void upload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        selectedFile = fileChooser.showOpenDialog(App.stage);
        if (selectedFile != null) {
            building_design_button.setText(selectedFile.getName());
        }

    }

    public void clear() {
        property_address_field.clear();
        owner_vendor_name_field.clear();
        license_number_field.clear();
        selectedFile = null;
        building_design_button.setText("Choose File");
        permitId.setText("");
    }

    @FXML
    private void cancel() throws IOException {
        App.setRoot("Main");
    }

    @FXML
    private void close() {
        System.exit(0);
        Platform.exit();
    }

    @FXML
    private void about() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("Real Estate");
        about.setContentText("Application Version: 1.0.0.0");
        about.setHeaderText("About Real Estate - Seller");
        about.show();
        about.getGraphic().getClass().getResourceAsStream("icon.png");
    }

}
