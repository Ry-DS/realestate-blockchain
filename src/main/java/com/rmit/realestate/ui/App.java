package com.rmit.realestate.ui;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.BlockData;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.PeerConnectionManager;
import com.rmit.realestate.blockchain.SecurityEntity;
import com.rmit.realestate.data.BuyerDao;
import com.rmit.realestate.data.SellerDao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {
    public static Scene scene;
    public static Stage stage;
    public static PeerConnectionManager peerConnectionManager;
    // TODO get from network
    private static Blockchain blockchain;
    // DAOs
    private static final SellerDao sellerDao = new SellerDao();
    private static final BuyerDao buyerDao = new BuyerDao();

    @Override
    public void start(Stage stage) throws IOException {
        // Security provider
        Security.addProvider(new BouncyCastleProvider());
        // Load private and public signing keys for all our users
        SecurityEntity.load();
        // Setup P2P networking instance
        peerConnectionManager = new PeerConnectionManager();
        // Start off with an empty blockchain unless we get something better from the network (longer)
        setBlockchain(new Blockchain());

        // Load UI
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

    /**
     * Attempts to publish this block to the blockchain, sending it to the network to be verified.
     *
     * @return true if successful, false otherwise
     */
    public static boolean publishBlock(BlockData data, SecurityEntity creator) {
        Block block = new Block(data, creator, blockchain.getBlocks().get(blockchain.getBlocks().size() - 1).getHash());
        List<Block> tmpBlockList = new ArrayList<>(blockchain.getBlocks());
        tmpBlockList.add(block);
        Blockchain tempBlockchain = new Blockchain(tmpBlockList);
        if (!data.verify(tempBlockchain, block)) return false;
        System.out.println("TODO");
        // TODO shouldn't do this.
        setBlockchain(tempBlockchain);
        return true;
        // Ready to send to network for authority to sign.
        // TODO send
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void setBlockchain(Blockchain blockchain) {
        App.blockchain = blockchain;
        sellerDao.updateFromBlockchain(blockchain);
        buyerDao.updateFromBlockchain(blockchain);
    }

    public static Blockchain getBlockchain() {
        return blockchain;
    }

    public static SellerDao getSellerDao() {
        return sellerDao;
    }

    public static BuyerDao getBuyerDao() {
        return buyerDao;
    }

    public static void main(String[] args) {
        launch();
    }

}