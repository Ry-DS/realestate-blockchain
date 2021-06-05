package com.rmit.realestate.blockchain;

public class Block<T> {
    private final T data;
    private final SecurityEntity creator;
    private final String hash;
    private final String signedHashByAdmin;
    private final String signedHashByCreator;
    private final String prevHash;
    private final long timestamp;

    public Block(T data, SecurityEntity creator, String hash, String signedHashByAdmin, String signedHashByCreator, String prevHash, long timestamp) {
        this.data = data;
        this.creator = creator;
        this.hash = hash;
        this.signedHashByAdmin = signedHashByAdmin;
        this.signedHashByCreator = signedHashByCreator;
        this.prevHash = prevHash;
        this.timestamp = timestamp;
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

    // TODO implement
    public String calculateHash() {
        return null;
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
     * @return true if verification succeeds, false othwerwise
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
