package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Blockchain;

/**
 * A goal of a DAO is to try find data which would be of use to the user,
 * and filtering out everything else.
 * For example, approved decisions aren't really needed by the UI
 */
public interface BlockchainDao {
    /**
     * When the blockchain is changed in this app, this should be called with the new one.
     * It is assumed the given blockchain has already been verified.
     * @param blockchain blockchian to extract data from.
     */
    void updateFromBlockchain(Blockchain blockchain);
}
