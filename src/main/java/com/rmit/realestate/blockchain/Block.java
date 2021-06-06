package com.rmit.realestate.blockchain;

import java.util.Date;

public class Block<T> {
    private final T data;
    private final SecurityEntity creator;
    private final String hash;
    private final String signedHashByAdmin;
    private final String signedHashByCreator;
    private final String prevHash;
    private final long timestamp;

    public Block(T data, SecurityEntity creator, String prevHash) throws Exception {
        this.data = data;
        this.creator = creator;
        this.prevHash = prevHash;
        this.timestamp = new Date().getTime();

        this.hash = calculateHash();
        this.signedHashByCreator = creator.sign(hash);
        // Block needs to now be broadcasted to the network for the admin to approve.
        this.signedHashByAdmin = null;
    }

    public T getData() {
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
        return Hashing.hash(data.toString(), creator.getName(), prevHash, timestamp);
    }

    public SecurityEntity getCreator() {
        return creator;
    }

    public String getSignedHashByAdmin() {
        return signedHashByAdmin;
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
