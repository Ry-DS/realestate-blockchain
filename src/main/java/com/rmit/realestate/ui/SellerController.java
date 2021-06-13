package com.rmit.realestate.ui;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.SecurityEntity;
import com.rmit.realestate.data.Seller;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class SellerController {
    private final SecurityEntity OWNER = SecurityEntity.SELLER;

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

    private final Color error = Color.web("#ff1c1c");

    public void submit() {
        permitId.setTextFill(error);
        String propertyAddress = property_address_field.getText();
        String owner = owner_vendor_name_field.getText();
        String license = license_number_field.getText();

        if (propertyAddress == null || propertyAddress.isBlank()) {

            permitId.setText("Address not given");
            return;
        }
        if (owner == null || owner.isBlank()) {

            permitId.setText("Owner/Vendor not given");
            return;
        }
        if (license == null || !license.matches("^L[0]+\\d$")) {

            permitId.setText("Licence Number invalid");
            return;
        }
        if (selectedFile == null) {
            permitId.setText("No File Selected");
            return;
        }
        // successful

        Seller seller = new Seller(propertyAddress, owner, license, selectedFile);
        permitId.setText("Submitting...");
        if (App.getSellerDao().addSeller(seller, OWNER)) {
            permitId.setTextFill(Color.GREEN);
            permitId.setText("Submitted - Permit Application Id: " + seller.getPermitId());
            // Save file in a folder with block hash. We don't explore syncing files like this in the blockchain
            // There are existing file blockchain impl. that can handle this.
            Block sellerBlock = App.getBlockchain().getBlocks().get(App.getBlockchain().getBlocks().size() - 1);
            try {
                File newFile = new File("pdfs");
                newFile.mkdir();
                newFile = new File(newFile, sellerBlock.getHash() + ".pdf");
                newFile.createNewFile();
                Files.copy(Paths.get(selectedFile.toURI()), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                System.err.println("Failed to save PDF");
                exception.printStackTrace();
            }
            submitClear();
        } else {
            permitId.setText("Failed to verify block on blockchain.");
        }

    }

    public void upload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        selectedFile = fileChooser.showOpenDialog(App.getStage());
        if (selectedFile != null) {
            building_design_button.setText(selectedFile.getName());
        }

    }

    public void clearButton() {
        submitClear();
        permitId.setText("");
    }

    public void submitClear() {
        property_address_field.clear();
        owner_vendor_name_field.clear();
        license_number_field.clear();
        selectedFile = null;
        building_design_button.setText("Choose File");
    }

    @FXML
    private void cancel() throws IOException {
        if (App.isAdmin())
            App.setRoot("Main");
        else
            App.setRoot("login");
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
        // TODO invalid
        // about.getGraphic().getClass().getResourceAsStream("icon.png");
    }

}
