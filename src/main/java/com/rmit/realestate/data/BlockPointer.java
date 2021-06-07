package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.BlockData;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.Hashing;

public abstract class BlockPointer implements BlockData {
    protected final String hashTarget;

    public BlockPointer(Block targetBlock) {
        this.hashTarget = targetBlock.getHash();
    }
    public Block getBlockPointer(Blockchain blockchain) {
        return blockchain.getBlocks().stream().filter(el -> el.getHash().equals(hashTarget)).findFirst().orElse(null);
    }

    @Override
    public String hash() {
        return Hashing.hash(hashTarget);
    }
}
