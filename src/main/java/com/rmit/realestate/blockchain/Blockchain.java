package com.rmit.realestate.blockchain;

import com.rmit.realestate.data.Seller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Blockchain {
    private final List<Block> blocks;

    public Blockchain(List<Block> blocks) {
        this.blocks = blocks;
    }

    public Blockchain() {
        this(new ArrayList<>());
    }

    public boolean verify() {
        for (int i = 1; i < blocks.size(); i++) {
            var currentBlock = blocks.get(i);
            var previousBlock = blocks.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.err.println("Hash of block " + i + " is incorrect.");

                return false;
            }
            if (!previousBlock.getHash().equals(currentBlock.getPrevHash())) {
                System.err.println("Previous hash doesn't match current block " + i);
                return false;
            }

            // Check if signatures are correct: Proof of Authority
            // if not signed by admin or owner of block
            if (!currentBlock.verifySignatures())
                return false;
            if (!currentBlock.getData().verify(this, currentBlock)) {
                return false;
            }


        }
        // All checks passed
        return true;
    }

    /**
     * Attempts to publish this block to the blockchain, sending it to the network to be verified.
     *
     * @return true if successful, false otherwise
     */
    public boolean publishBlock(BlockData data, SecurityEntity creator) throws Exception {
        Block block = new Block(data, creator, blocks.get(blocks.size() - 1).getHash());
        Blockchain tempBlockchain = new Blockchain(blocks);
        tempBlockchain.blocks.add(block);
        if (!data.verify(tempBlockchain, block)) return false;
        return true;
        // Ready to send to network for authority to sign.
        // TODO send
    }

    public Collection<Block> getBlocks() {
        return Collections.unmodifiableCollection(blocks);
    }

    public Block findBlockWithData(BlockData blockData) {
        return getBlocks().stream().filter(b -> b.getData() == blockData).findFirst().orElse(null);
    }
}
