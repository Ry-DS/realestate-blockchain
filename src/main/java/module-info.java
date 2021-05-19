module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.rmit.blockchain to javafx.fxml;
    exports com.rmit.blockchain;
    exports com.rmit.blockchain.data;
    opens com.rmit.blockchain.data to javafx.fxml;
}