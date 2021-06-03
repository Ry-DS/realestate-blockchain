package com.rmit.realestate.ui;

import com.rmit.realestate.data.Seller;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

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

//        System.out.println("awd"+propertyAddress.length());
//        System.out.println("zbcde".charAt(0));
//        System.out.println("zbcde".substring(1));

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
        if (propertyAddress != null && owner != null && license != null && selectedFile != null){
            permitId.setTextFill(Color.BLACK);
            permitId.setText("Permit Application Id: " + "00001");
            return;
        }
    }

    public void upload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
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
