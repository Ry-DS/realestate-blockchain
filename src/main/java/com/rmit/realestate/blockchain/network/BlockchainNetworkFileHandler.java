package com.rmit.realestate.blockchain.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.SecurityEntity;
import com.rmit.realestate.data.ApplicationStatus;
import com.rmit.realestate.data.Buyer;
import com.rmit.realestate.data.EntityDecision;
import com.rmit.realestate.data.Seller;
import com.rmit.realestate.ui.App;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;

public class BlockchainNetworkFileHandler extends P2PListener {
    private static final String FILE_NAME = "blockchain.raw";
    private final Kryo kryo;
    private final PeerConnectionManager pcm;

    public BlockchainNetworkFileHandler(PeerConnectionManager pcm) {
        this.pcm = pcm;
        Log.NONE();
        kryo = pcm.getServer().getKryo();
        registerClasses(kryo);
        pcm.addNetworkListener(this);

    }

    private void registerClasses(Kryo kryo) {
        // Blockchain
        kryo.register(Blockchain.class);
        kryo.register(Block.class);
        // Blockchain Data
        kryo.register(Seller.class);
        kryo.register(Buyer.class);
        kryo.register(EntityDecision.class);
        // Packets
        kryo.register(BouncePacket.class);
        // Util
        kryo.register(ArrayList.class);
        kryo.register(HashSet.class);
        kryo.register(SecurityEntity.class);
        kryo.register(ApplicationStatus.class);
        //UnmodifiableCollectionsSerializer.registerSerializers(kryo);
    }

    // When we connect to another peer to listen from.
    // Warning: Connection hasn't been made yet to receive data.
    @Override
    public void onClientJoinAttempt(Client client) {
        registerClasses(client.getKryo());
    }

    // When a new peer connects, send them our copy of the blockchain.
    @Override
    public void onServerConnect(Connection connection) {
        connection.sendTCP(new BouncePacket<>(App.getBlockchain(), pcm.getPort()));
    }

    // When we receive a message from one of the clients we're connected to.
    @Override
    public void onIncomingData(Object o, InetSocketAddress conn) {
        Object data;
        if (o instanceof BouncePacket) {
            BouncePacket<?> packet = (BouncePacket<?>) o;

            if (!packet.getViewed().contains(pcm.getPort())) {
                // Add self to viewed list
                data = packet.getData();
                packet.addPort(pcm.getPort());
                pcm.broadcastMessage(packet);
            } else data = null;

        } else data = o;

        if (data instanceof Blockchain) {
            Blockchain chain = (Blockchain) data;

            // This blockchain has more "work" in it and is valid so we'll use this instead.
            if (chain.getBlocks().size() > App.getBlockchain().getBlocks().size() && chain.verify()) {
                System.out.println("Received a longer chain: " + chain);
                App.setBlockchain(chain);
            }
        }
    }

    public Blockchain loadBlockchainFromFile() throws IOException {

        try (Input input = new Input(new FileInputStream(FILE_NAME))) {
            Blockchain blockchain = kryo.readObject(input, Blockchain.class);
            System.out.println(blockchain);
            return blockchain;
        }
    }

    public void saveBlockchainToFile(Blockchain blockchain) throws IOException {
        try (Output output = new Output(new FileOutputStream(FILE_NAME))) {
            kryo.writeObject(output, blockchain);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
