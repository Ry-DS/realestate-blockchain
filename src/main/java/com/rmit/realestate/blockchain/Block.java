package com.rmit.realestate.blockchain;

public class Block<T> {
    private final T data;
    private final String hash;
    private final String signedHashByAuthority;
    private final String signedHashByPublisher;
    private final String prevHash;
    private final long timestamp;

    public Block(T data, String hash, String signedHashByAuthority, String signedHashByPublisher, String prevHash, long timestamp) {
        this.data = data;
        this.hash = hash;
        this.signedHashByAuthority = signedHashByAuthority;
        this.signedHashByPublisher = signedHashByPublisher;
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

    public String calculateHash() {
        return null;
    }
}
