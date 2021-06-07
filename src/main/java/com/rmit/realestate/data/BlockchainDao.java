package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Blockchain;

public interface BlockchainDao {
    /**
     * When the blockchain is changed in this app, this should be called with the new one.
     * It is assumed the given blockchain has already been verified.
     * @param blockchain blockchian to extract data from.
     */
    void updateFromBlockchain(Blockchain blockchain);
}
