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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockchainNetworkFileHandler extends P2PListener {
    private static final String FILE_NAME = "blockchain.raw";
    // The max amount of time we're willing to wait for our block to be verified by the network before failing.
    private static final int BLOCK_WAIT_TIMEOUT = 1000;
    private final Kryo kryo;
    private final PeerConnectionManager pcm;
    // This map holds blocks the user submitted. We are waiting for a response from the network to sign our block
    // before we can add it to our own chain.
    // Map<BlockHash, ThreadLock>
    private final Map<String, Object> waitingBlocks = new ConcurrentHashMap<>();

    public BlockchainNetworkFileHandler(PeerConnectionManager pcm) {
        this.pcm = pcm;
        Log.NONE();
        kryo = pcm.getServer().getKryo();
        registerClasses(kryo);
        pcm.addNetworkListener(this);

    }
    // When a new peer connects, send them our copy of the blockchain.
    @Override
    public void onServerConnect(Connection connection) {
        connection.sendTCP(App.getBlockchain());
    }
    // When we receive a message from one of the clients we're connected to.
    @Override
    public void onIncomingData(Object o) {
        Object data;
        if (o instanceof BouncePacket) {
            BouncePacket<?> packet = (BouncePacket<?>) o;
            // Bounce packets could also be used to find all clients on the network,
            // But this isn't implemented here to keep things simple.
            if (!packet.getViewed().contains(pcm.getPort())) {
                // Add self to viewed list
                data = packet.getData();
                packet.addPort(pcm.getPort());
                // TODO disable bounce functionality. It isn't very stable
                //   pcm.broadcastMessage(packet);
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
        if (data instanceof Block) {
            Block newBlock = (Block) data;
            if (!newBlock.verifySignatures()) {
                // If the new block hasn't been verified we can verify it ourselves and send it into the network.
                if (App.isAdmin())
                    broadcastAndAddBlockToNetwork(newBlock, App.getBlockchain());
                // No point working with an unverified block
                return;
            }
            Blockchain newBlockchain = getNewBlockchainWithBlock(newBlock, App.getBlockchain());
            if (!newBlockchain.verify()) {
                // We were given an invalid block. Let's send out our chain again in case they had the wrong one.
                pcm.broadcastMessage(App.getBlockchain());
                return;
            }
            // We have a valid block! Begin saving it and notify anyone waiting for this block.
            Object blockLock = waitingBlocks.entrySet().stream().filter(l -> l.getKey().equals(newBlock.getHash())).
                    map(Map.Entry::getValue).findFirst().orElse(null);
            if (blockLock != null) {
                synchronized (blockLock) {
                    App.setBlockchain(newBlockchain);
                    blockLock.notify();
                }
            } else App.setBlockchain(newBlockchain);

        }
    }

    private Blockchain getNewBlockchainWithBlock(Block block, Blockchain blockchain) {
        List<Block> tmpBlockList = new ArrayList<>(blockchain.getBlocks());
        tmpBlockList.add(block);
        return new Blockchain(tmpBlockList);
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

    /**
     * Blocking method which broadcasts block to network, then doesn't return until the block is
     * registered onto the blockchain, or a timeout is reached.
     *
     * @param block         Block to broadcast.
     * @param oldBlockchain current blockchain before new block is added.
     * @return true if block was added to chain, false otherwise. Block is verified before submission.
     */
    public boolean broadcastAndAddBlockToNetwork(Block block, Blockchain oldBlockchain) {
        // Verify data, we don't need to verify hashes or signature since that's still up in the air
        {
            Blockchain tempBlockchain = getNewBlockchainWithBlock(block, oldBlockchain);
            if (!block.getData().verify(tempBlockchain, block))
                return false;
        }
        if (App.isAdmin()) {
            // If we're admin, we can sign our own block and send it on its way to other users.
            block.setSignedByAdmin();
            List<Block> newBlockList = new ArrayList<>(oldBlockchain.getBlocks());
            newBlockList.add(block);
            Blockchain newBlockchain = new Blockchain(newBlockList);
            if (newBlockchain.verify()) {
                pcm.broadcastMessage(new BouncePacket<>(block, pcm.getPort()));
                App.setBlockchain(newBlockchain);
                return true;
            }
            return false;
        }

        Object waitingLock = new Object();
        waitingBlocks.put(block.getHash(), waitingLock);
        pcm.broadcastMessage(new BouncePacket<>(block, pcm.getPort()));
        // Wait to see if our new block was submitted.
        synchronized (waitingLock) {
            try {
                waitingLock.wait(BLOCK_WAIT_TIMEOUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Check whether the new block was added while we were waiting.
        return App.getBlockchain().getBlocks().stream().anyMatch(b -> b.getHash().equals(block.getHash()));
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
        // Not supported by this version
        // UnmodifiableCollectionsSerializer.registerSerializers(kryo);
    }

    // When we connect to another peer to listen from.
    // Warning: Connection hasn't been made yet to receive data.
    @Override
    public void onClientJoinAttempt(Client client) {
        registerClasses(client.getKryo());
    }


}
