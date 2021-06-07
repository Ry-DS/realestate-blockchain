package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.BlockData;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.Hashing;

/**
 * Block data which points to another block in the chain.
 */
public abstract class BlockPointer implements BlockData {
    private final String hashTarget;

    public BlockPointer(Block targetBlock) {
        this.hashTarget = targetBlock.getHash();
    }
    public Block getBlockPointer(Blockchain blockchain) {
        return blockchain.getBlocks().stream().filter(el -> el.getHash().equals(hashTarget)).findFirst().orElse(null);
    }

    public String getHashTarget() {
        return hashTarget;
    }

    @Override
    public String hash() {
        return Hashing.hash(hashTarget);
    }
}
