package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.Verifiable;

public class EntityDecision<T> implements Verifiable {
    private final transient Block<T> entity;
    private final String hashTarget;
    private final ApplicationStatus status;

    public EntityDecision(Block<T> entity, ApplicationStatus status) {
        this.entity = entity;
        this.status = status;
        hashTarget = entity.getHash();
    }

    public T getEntity() {
        return entity.getData();
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    @Override
    public boolean verify(Blockchain blockchain) {
        return blockchain.getBlocks().stream().anyMatch(block -> block.getHash().equals(hashTarget));
    }
}
