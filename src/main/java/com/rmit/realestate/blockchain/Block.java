package com.rmit.realestate.blockchain;

public class Block<T> {
    private final T data;
    private final String hash;
    private final String prevHash;
    private final int nonce;
    private final long timestamp;

    public Block(T data, String hash, String prevHash, int nonce, long timestamp) {
        this.data = data;
        this.hash = hash;
        this.prevHash = prevHash;
        this.nonce = nonce;
        this.timestamp = timestamp;
    }
}
