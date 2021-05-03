package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class SellerController {
    @FXML
    TextField property_address_field;
    @FXML
    TextField owner_vendor_name_field;
    @FXML
    Button building_design_button;
    @FXML
    TextField licence_number_field;

    @FXML
    Label permitId;


    public void submit() {

        property_address_field.clear();
        owner_vendor_name_field.clear();
        licence_number_field.clear();
        permitId.setText("Permit Id: " + "3");

    }

    public void upload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
               );
        File selectedFile = fileChooser.showOpenDialog(App.stage);
        if (selectedFile != null) {
            building_design_button.setText(selectedFile.getName());
        }

    }

    @FXML
    private void cancel() throws IOException {
        App.setRoot("Main");
    }
}
