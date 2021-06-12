package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.SecurityEntity;
import com.rmit.realestate.ui.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Stream;

public class SellerDao implements BlockchainDao {
    private final ObservableList<Seller> pendingSellers = FXCollections.observableArrayList();
    private final ObservableList<Seller> approvedSellers = FXCollections.observableArrayList();

    /**
     * Adds a seller and returns the permit id.
     */
    public boolean addSeller(Seller seller, SecurityEntity entity) {
        int id = App.getBlockchain().getBlocks().size();
        seller.setPermitId(id);
        return App.publishBlock(seller, entity);
    }


    public boolean approve(Seller seller, SecurityEntity entity) {
        EntityDecision decision = new EntityDecision(App.getBlockchain().findBlockWithData(seller), ApplicationStatus.APPROVED);
        return App.publishBlock(decision, entity);
    }

    public boolean disapprove(Seller seller, SecurityEntity entity) {
        EntityDecision decision = new EntityDecision(App.getBlockchain().findBlockWithData(seller), ApplicationStatus.DENIED);
        return App.publishBlock(decision, entity);

    }

    @Override
    public void updateFromBlockchain(Blockchain blockchain) {
        pendingSellers.clear();
        approvedSellers.clear();
        // O(n^2) worst case. Could improve with map but this isn't an algorithms course
        for (Block sellerBlock : blockchain.getBlocks()) {
            if (sellerBlock.getData() instanceof Seller) {
                Seller seller = (Seller) sellerBlock.getData();
                // Whether this seller was approved by the authority.
                ApplicationStatus sellerDecision;
                // Try work out the approved/deny status of this seller
                sellerDecision = blockchain.getBlocks().stream()
                        .filter(b -> b.getData() instanceof EntityDecision)
                        .map(b -> (EntityDecision) b.getData())
                        .filter(b -> b.getHashTarget().equals(sellerBlock.getHash()))
                        .map(EntityDecision::getStatus)
                        .findFirst().orElse(null);
                if (sellerDecision == ApplicationStatus.APPROVED) {
                    // Try work out if there's already a buy in progress.
                    BuyerDao buyerDao = new BuyerDao();
                    buyerDao.updateFromBlockchain(blockchain);
                    // If a buy isn't currently pending or approved for this seller, we can add it to approved for other buyers
                    if (Stream.of(buyerDao.getApprovedBuyers(), buyerDao.getPendingBuyers()).flatMap(List::stream)
                            .noneMatch(b -> b.getHashTarget().equals(sellerBlock.getHash())))
                        approvedSellers.add(seller);
                } else if (sellerDecision == null)
                    pendingSellers.add(seller);
            }
        }
    }

    public ObservableList<Seller> getApprovedSellers() {

        return FXCollections.unmodifiableObservableList(approvedSellers);
    }

    public ObservableList<Seller> getPendingSellers() {
        return FXCollections.unmodifiableObservableList(pendingSellers);
    }
}
