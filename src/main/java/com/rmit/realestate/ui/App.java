package com.rmit.realestate.ui;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.BlockData;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.SecurityEntity;
import com.rmit.realestate.blockchain.network.BlockchainNetworkFileHandler;
import com.rmit.realestate.blockchain.network.PeerConnectionManager;
import com.rmit.realestate.data.BuyerDao;
import com.rmit.realestate.data.SellerDao;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Security;

/**
 * JavaFX App
 */
public class App extends Application {
    // Whether this app instance will be responsible for verifying blocks onto the blockchain.
    private static boolean isAdmin = false;
    private static Scene scene;
    private static Stage stage;
    private static PeerConnectionManager peerConnectionManager;
    private static BlockchainNetworkFileHandler blockchainHandler;

    // TODO get from network
    private static Blockchain blockchain;
    private static final IntegerProperty blockchainSize = new SimpleIntegerProperty(1);
    // DAOs
    private static final SellerDao sellerDao = new SellerDao();
    private static final BuyerDao buyerDao = new BuyerDao();

    private static void initialize(int port, int p2pPort) throws IOException {
        if (peerConnectionManager != null)
            throw new IllegalStateException("Already initialized");
        // Security provider
        Security.addProvider(new BouncyCastleProvider());
        // Load private and public signing keys for all our users
        SecurityEntity.load();
        // Start P2P network
        peerConnectionManager = new PeerConnectionManager(port, p2pPort);
        blockchainHandler = new BlockchainNetworkFileHandler(peerConnectionManager);

        Blockchain fromFile = null;
        try {
            fromFile = blockchainHandler.loadBlockchainFromFile();
        } catch (FileNotFoundException ignored) {
        }
        if (fromFile == null || !fromFile.verify()) {
            System.out.println("Failed to find/load blockchain file. Making a new chain...");
            setBlockchain(new Blockchain());
        } else {
            System.out.println("Loaded a blockchain with " + fromFile.getBlocks().size() + " blocks from file.");
            setBlockchain(fromFile);
        }
        peerConnectionManager.startThreads();
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Load UI
        scene = new Scene(loadFXML("login"));
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
     * This is a blocking operation, and won't return unless there's a timeout, or the block has successfully been added.
     *
     * @return true if successful, false otherwise
     */
    public static boolean publishBlock(BlockData data, SecurityEntity creator) {
        Block block = new Block(data, creator, blockchain.getBlocks().get(blockchain.getBlocks().size() - 1).getHash());
        return blockchainHandler.broadcastAndAddBlockToNetwork(block, blockchain);
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void setBlockchain(Blockchain blockchain) {
        if (!blockchain.verify())
            throw new IllegalStateException("Tried to set an unverified blockchain.");
        App.blockchain = blockchain;
        sellerDao.updateFromBlockchain(blockchain);
        buyerDao.updateFromBlockchain(blockchain);
        Platform.runLater(() -> blockchainSize.set(blockchain.getBlocks().size()));
        try {
            blockchainHandler.saveBlockchainToFile(blockchain);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Failed to save blockchain");
        }
    }

    public static Blockchain getBlockchain() {
        return blockchain;
    }

    public static IntegerProperty blockchainSizeProperty() {
        return blockchainSize;
    }

    public static SellerDao getSellerDao() {
        return sellerDao;
    }

    public static BuyerDao getBuyerDao() {
        return buyerDao;
    }

    /**
     * True if the current instance has the power to verify its own blocks, and the blocks of others.
     */
    public static boolean isAdmin() {
        return isAdmin;
    }

    public static Scene getScene() {
        return scene;
    }

    public static Stage getStage() {
        return stage;
    }

    public static PeerConnectionManager getPeerConnectionManager() {
        return peerConnectionManager;
    }

    public static void main(String[] args) throws IOException {
        int port = 1000;
        int p2pPort = port;

        for (String arg : args) {
            if (arg.startsWith("--port=")) {
                port = Integer.parseInt(arg.split("=")[1]);
            }
            if (arg.startsWith("--p2pPort=")) {
                p2pPort = Integer.parseInt(arg.split("=")[1]);
            }
            if (arg.equals("--admin"))
                isAdmin = true;
        }
        if (port < p2pPort || port > p2pPort + PeerConnectionManager.PORT_SEARCH_RANGE)
            throw new IllegalArgumentException("Port must be within the range of P2P port range: " + p2pPort + " to " + (p2pPort + PeerConnectionManager.PORT_SEARCH_RANGE));


        App.initialize(port, p2pPort);
        launch(args);
    }

}