module com.rmit.realestate {
    requires javafx.controls;
    requires javafx.fxml;
    requires kryonet;

    opens com.rmit.realestate.ui to javafx.fxml;
    opens com.rmit.realestate.data to javafx.fxml;
    exports com.rmit.realestate.ui;
    exports com.rmit.realestate.data;
    exports com.rmit.realestate.blockchain;
}