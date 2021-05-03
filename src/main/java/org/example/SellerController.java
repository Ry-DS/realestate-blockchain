package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class SellerController {
    @FXML
    TextField property_address_field;
    @FXML
    TextField owner_vendor_name_field;
    @FXML
    TextField building_design_field;
    @FXML
    TextField licence_number_field;


    public void submit(){

property_address_field.clear();
owner_vendor_name_field.clear();
building_design_field.clear();
licence_number_field.clear();

    }


    @FXML
    private void cancel() throws IOException {
        App.setRoot("Main");
    }
}
