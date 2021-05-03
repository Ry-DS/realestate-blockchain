package org.example;

import javafx.fxml.FXML;

import java.io.IOException;

public class LoginController {
    @FXML
    private void cancel() throws IOException {
        App.setRoot("Main");
    }
}
