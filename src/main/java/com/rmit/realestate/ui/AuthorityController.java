package com.rmit.realestate.ui;

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
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.List;

public class AuthorityController {

    @FXML
    ComboBox<String> addressProperty;
    @FXML
    Label message;

    @FXML
    TableView<List> authorityTable;  // For Table

    @FXML
    private TableColumn<Seller, String> addressTable; // For Table

    @FXML
    private TableColumn<Seller, String> ownerTable; // For Table

    ObservableList<String> list = FXCollections.observableArrayList("House 1","House 2");

    public void initialize(){
        addressProperty.setItems(list);
        authorityTable.getItems().add(list); // For Table
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


    public void cancel() throws IOException{
        App.setRoot("Main");
    }

    public void accept(){
        String addressProperty1 = addressProperty.getValue();
        if (addressProperty1 == null || addressProperty1.isBlank()){
            message.setText("Please Select a Property");
        }

        if (addressProperty1 != null){
            message.setTextFill(Color.GREEN);
            message.setText("Property has been approved");
        }
    }

    public void decline(){
        String addressProperty1 = addressProperty.getValue();
        if (addressProperty1 == null || addressProperty1.isBlank()){
            message.setText("Please Select a Property");
        }

        if (addressProperty1 != null){
            message.setTextFill(Color.GREEN);
            message.setText("Property has been declined");
        }
    }


}