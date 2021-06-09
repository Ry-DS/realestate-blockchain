package com.rmit.realestate.blockchain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

/**
 * Immutable representation of a blockchain
 */
public class Blockchain {
    private final List<Block> blocks;

    public Blockchain(List<Block> blocks) {
        this.blocks = blocks;
    }

    public Blockchain() {
        // Genesis block. We can only use ArrayList class here, since that's serializable by kryo
        this(new ArrayList<>(Collections.singletonList(new Block(null, null, null))));
    }

    public boolean verify() {
        if (blocks.isEmpty())
            return false;
        // Verify the genesis block has null data.
        if (blocks.get(0).getData() != null) {
            System.err.println("Invalid Genesis Block");
            return false;
        }
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
            if (!currentBlock.verifySignatures()) {
                System.err.println("Signature check failed on " + i);
                return false;
            }
            // Only admin can create blocks with null data
            if (currentBlock.getData() == null && currentBlock.getCreator() != SecurityEntity.BLOCKCHAIN_ADMIN) {
                System.err.println("Block data is null on " + i);
                return false;
            }
            // Verify data to make sure its clear of defects
            if (!currentBlock.getData().verify(this, currentBlock)) {
                System.err.println("Data verification failed on " + i + ", datatype: "
                        + currentBlock.getData().getClass().getSimpleName());
                return false;
            }
            // Verify a block didn't happen in the past of the previous block
            if (currentBlock.getTimestamp() < previousBlock.getTimestamp()) {
                System.err.println("Block " + i + " timestamp is invalid.");
                return false;
            }

        }
        // All checks passed
        return true;
    }

    public List<Block> getBlocks() {
        return Collections.unmodifiableList(blocks);
    }

    // Exists for debugging to console
    @Override
    public String toString() {
        return new StringJoiner(", ", Blockchain.class.getSimpleName() + "[", "]")
                .add("blocks=" + blocks)
                .toString();
    }

    public Block findBlockWithData(BlockData blockData) {
        if (blockData == null)
            return null;
        return getBlocks().stream().filter(b -> b.getData() == blockData).findFirst().orElse(null);
    }
}
