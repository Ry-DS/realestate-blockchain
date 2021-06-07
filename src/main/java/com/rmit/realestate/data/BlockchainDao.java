package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Blockchain;

public interface BlockchainDao {
    void updateFromBlockchain(Blockchain blockchain);
}
