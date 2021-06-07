package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Blockchain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SellerDao implements BlockchainDao {
    private static final ObservableList<Seller> pendingSellers = FXCollections.observableArrayList();
    private static final ObservableList<Seller> approvedSellers = FXCollections.observableArrayList();

    /**
     * Adds a seller and returns the permit id.
     */
    public static int addSeller(Seller seller) {
        // TODO
        pendingSellers.add(seller);
        int id = pendingSellers.size();
        seller.setLicenseNumber(id);
        return id;
    }

    public static ObservableList<Seller> getPendingSellers() {
        return FXCollections.unmodifiableObservableList(pendingSellers);
    }


    public static void approve(Seller seller) {
        // TODO
        pendingSellers.remove(seller);
        approvedSellers.add(seller);
        getApprovedSellers();


    }

    public static void disapprove(Seller seller) {
        // TODO
        pendingSellers.remove(seller);
    }

    public static ObservableList<Seller> getApprovedSellers() {

        return FXCollections.unmodifiableObservableList(approvedSellers);
    }

    @Override
    public void updateFromBlockchain(Blockchain blockchain) {
        pendingSellers.clear();
        approvedSellers.clear();
        // TODO update from blockchain.
    }
}
