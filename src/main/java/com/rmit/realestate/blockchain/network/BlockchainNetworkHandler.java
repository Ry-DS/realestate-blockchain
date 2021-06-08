package com.rmit.realestate.blockchain.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.BlockData;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.SecurityEntity;
import com.rmit.realestate.ui.App;

import java.util.ArrayList;
import java.util.function.Consumer;

public class BlockchainNetworkHandler extends P2PListener {
    PeerConnectionManager pcm;

    public BlockchainNetworkHandler(PeerConnectionManager pcm) {
        this.pcm = pcm;
        Log.NONE();
        Kryo kryo = pcm.getServer().getKryo();
        registerClasses(kryo);



    }

    private void registerClasses(Kryo kryo) {
        kryo.register(Blockchain.class);
        kryo.register(BlockData.class);
        kryo.register(ArrayList.class);
        kryo.register(Block.class);
        kryo.register(SecurityEntity.class);
        //UnmodifiableCollectionsSerializer.registerSerializers(kryo);
    }

    @Override
    public void onClientJoinAttempt(Client client) {
        registerClasses(client.getKryo());
    }
    // When a new peer connects, send them our copy of the blockchain.

    @Override
    public void onServerConnect(Connection connection) {
        connection.sendTCP(App.getBlockchain());
    }

    // When we receive a message from one of the clients we're connected to.
    @Override
    public void onIncomingData(Object o) {
        if (o instanceof Blockchain) {
            Blockchain chain = (Blockchain) o;
            System.out.println("Received chain: " + chain);
            // This blockchain has more "work" in it and is valid so we'll use this instead.
            if (chain.getBlocks().size() > App.getBlockchain().getBlocks().size() && chain.verify()) {
                App.setBlockchain(chain);
            }
        }
    }

}
