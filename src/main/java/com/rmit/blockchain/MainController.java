package com.rmit.blockchain;

import javafx.fxml.FXML;

import java.io.IOException;

public class MainController {
    @FXML
    private void BuyerForm() throws IOException {
        App.setRoot("BuyerForm");
    }

    @FXML
    private void SellerForm() throws IOException {
        App.setRoot("seller");
    }

    @FXML
    private void Login() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void Authority() throws IOException {
        App.setRoot("Authority");
    }

    @FXML
    private void Bank() throws IOException {
        App.setRoot("Bank");
    }
}
