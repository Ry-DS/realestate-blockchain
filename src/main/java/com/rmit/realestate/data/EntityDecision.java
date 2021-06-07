package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.BlockData;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.Hashing;
import com.rmit.realestate.blockchain.SecurityEntity;

public class EntityDecision implements BlockData {
    private final String hashTarget;
    private final ApplicationStatus status;

    public EntityDecision(Block entity, ApplicationStatus status) {
        this.status = status;
        hashTarget = entity.getHash();
    }

    public Block getBlockPointer(Blockchain blockchain) {
        return blockchain.getBlocks().stream().filter(el -> el.getHash().equals(hashTarget)).findFirst().orElse(null);
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    @Override
    public boolean verify(Blockchain blockchain, Block container) {
        Block sellerOrBuyerBlock = getBlockPointer(blockchain);
        // Block doesn't exist.
        if (sellerOrBuyerBlock == null)
            return false;
        for (Block block : blockchain.getBlocks()) {
            EntityDecision existingDecision = block.getData() instanceof EntityDecision ? (EntityDecision) block.getData() : null;
            // Block already has a decision made for it.
            if (existingDecision != null && existingDecision.hashTarget.equals(this.hashTarget) && existingDecision != this)
                return false;
        }
        switch (container.getCreator()) {
            case BANK:
                return sellerOrBuyerBlock.getCreator() == SecurityEntity.BUYER;
            case AUTHORITY:
                return sellerOrBuyerBlock.getCreator() == SecurityEntity.SELLER;
            // No other user type can make decisions
            default:
                return false;
        }
    }

    @Override
    public String hash() {
        return Hashing.hash(hashTarget, status);
    }
}
