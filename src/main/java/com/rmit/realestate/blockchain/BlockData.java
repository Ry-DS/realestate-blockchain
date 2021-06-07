package com.rmit.realestate.blockchain;

public interface BlockData {
    boolean verify(Blockchain blockchain, Block container);

    String hash();
}
