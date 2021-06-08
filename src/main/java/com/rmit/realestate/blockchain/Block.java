package com.rmit.realestate.blockchain;

import java.security.GeneralSecurityException;
import java.util.Date;

public class Block {
    private final BlockData data;
    private final SecurityEntity creator;
    private final String hash;
    private String signedHashByAdmin;
    private final String signedHashByCreator;
    private final String prevHash;
    private final long timestamp;

    public Block(BlockData data, SecurityEntity creator, String prevHash) {
        this.data = data;
        this.creator = creator;
        this.prevHash = prevHash;
        this.timestamp = new Date().getTime();

        this.hash = calculateHash();
        try {
            this.signedHashByCreator = creator.sign(hash);
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to sign block for creator: " + creator);
        }

        // Block needs to now be broadcasted to the network for the admin to approve.
        this.signedHashByAdmin = null;
    }

    public BlockData getData() {
        return data;
    }

    public String getHash() {
        return hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String calculateHash() {
        return Hashing.hash(data != null ? data.toString() : null, creator.getName(), prevHash, timestamp);
    }

    public SecurityEntity getCreator() {
        return creator;
    }

    public String getSignedHashByAdmin() {
        return signedHashByAdmin;
    }

    /**
     * Sign this block and make it completely valid. Should only be called if this app user is a blockchain admin.
     * In reality, the key used here would only be accessible to a blockchain admin.
     *
     * @return the same block for chaining.
     */
    protected Block setSignedByAdmin() {
        try {
            this.signedHashByAdmin = SecurityEntity.BLOCKCHAIN_ADMIN.sign(hash);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to sign block for blockchain admin");
        }
        return this;
    }

    public String getSignedHashByCreator() {
        return signedHashByCreator;
    }

    /**
     * Verify if {@link #signedHashByAdmin} and {@link #signedHashByCreator}
     * is correct with public key.
     *
     * @return true if verification succeeds, false otherwise
     */
    public boolean verifySignatures() {
        try {
            boolean creatorVerified = creator.verify(hash, signedHashByCreator);
            boolean adminVerified = SecurityEntity.BLOCKCHAIN_ADMIN.verify(hash, signedHashByAdmin);
            return adminVerified && creatorVerified;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Warning! Failed to verify signatures of block: " + hash);
            return false;
        }

    }
}
