module com.rmit.realestate {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.bouncycastle.provider;
    requires kryo;
    requires kryonet;
    requires minlog;
   // requires kryo.serializers;

    opens com.rmit.realestate.ui to javafx.fxml;
    opens com.rmit.realestate.data to javafx.fxml, kryo;
    opens com.rmit.realestate.blockchain to kryo;
    opens com.rmit.realestate.blockchain.network to kryo;
    exports com.rmit.realestate.ui;
    exports com.rmit.realestate.data;
    exports com.rmit.realestate.blockchain;
    exports com.rmit.realestate.blockchain.network;
}