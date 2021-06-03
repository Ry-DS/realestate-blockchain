package com.rmit.realestate.ui;

import com.rmit.realestate.data.Seller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class AuthorityController {

    @FXML
    ComboBox<String> addressProperty;
    @FXML
    Label message;

    @FXML
    TableView<Seller> authorityTable;

    @FXML
    private TableColumn<Seller, String> addressTable;

    @FXML
    private TableColumn<Seller, String> ownerTable;

//    Seller seller = new Seller("House", "Gerald", "Gerald", null, "L001" );
    ObservableList<String> list = FXCollections.observableArrayList("House 1","House 2");
//    ObservableList<String> list1 = (ObservableList<String>) seller;

    public void initialize(){
        Seller seller = new Seller("House", "Gerald", "Gerald", null, "L001" );
        addressProperty.setItems(list);
        authorityTable.getItems().add(seller);
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
        String addressProperty1 = (String) addressProperty.getValue();
        if (addressProperty1 == null || addressProperty1.isBlank()){
            message.setText("Please Select a Property");
        }

        if (addressProperty1 != null){
            message.setTextFill(Color.GREEN);
            message.setText("Property has been approved");
        }
    }

    public void decline(){
        String addressProperty1 = (String) addressProperty.getValue();
        if (addressProperty1 == null || addressProperty1.isBlank()){
            message.setText("Please Select a Property");
        }

        if (addressProperty1 != null){
            message.setTextFill(Color.GREEN);
            message.setText("Property has been declined");
        }
    }


}
