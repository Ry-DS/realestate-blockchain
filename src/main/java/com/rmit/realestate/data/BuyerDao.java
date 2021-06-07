package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Blockchain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuyerDao implements BlockchainDao {
    private static final ObservableList<Buyer> buyers = FXCollections.observableArrayList();

    /**
     * Adds a seller and returns the permit id.
     */
    public static int addBuyer(Buyer buyer) {
        // TODO
        buyers.add(buyer);
        int id = buyers.size();
        buyer.setLoanApplicationId(id);
        return id;
    }

    public static ObservableList<Buyer> getBuyers() {
        return FXCollections.unmodifiableObservableList(buyers);
    }

    public static void approve(Buyer buyer) {
        // TODO
        buyers.remove(buyer);
    }

    public static void disapprove(Buyer buyer) {
        // TODO
        buyers.remove(buyer);
    }

    @Override
    public void updateFromBlockchain(Blockchain blockchain) {
        buyers.clear();
    }
}
