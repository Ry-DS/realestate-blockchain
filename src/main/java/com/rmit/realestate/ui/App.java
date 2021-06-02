package com.rmit.realestate.ui;

import com.rmit.realestate.blockchain.PeerConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.Security;

/**
 * JavaFX App
 */
public class App extends Application {

    public static Scene scene;
    public static Stage stage;
    public static PeerConnectionManager peerConnectionManager;

    @Override
    public void start(Stage stage) throws IOException {
        Security.addProvider(new BouncyCastleProvider());
        peerConnectionManager = new PeerConnectionManager();
        scene = new Scene(loadFXML("Main"));
        stage.setScene(scene);
        App.stage = stage;
        stage.show();
        stage.setResizable(false);
        stage.setWidth(902);
        stage.setHeight(552);
        stage.setTitle("Blockchain Application");
        Image icon = new Image(getClass().getResourceAsStream("icon.png"));
        stage.getIcons().add(icon);
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}