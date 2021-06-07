package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.SecurityEntity;
import com.rmit.realestate.ui.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// TODO link with blockchain
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

    public ObservableList<Seller> getPendingSellers() {
        return FXCollections.unmodifiableObservableList(pendingSellers);
    }


    public boolean approve(Seller seller, SecurityEntity entity) {
        EntityDecision decision = new EntityDecision(App.getBlockchain().findBlockWithData(seller), ApplicationStatus.APPROVED);
        return App.publishBlock(decision, entity);
    }

    public boolean disapprove(Seller seller, SecurityEntity entity) {
        EntityDecision decision = new EntityDecision(App.getBlockchain().findBlockWithData(seller), ApplicationStatus.DENIED);
        return App.publishBlock(decision, entity);

    }

    public ObservableList<Seller> getApprovedSellers() {

        return FXCollections.unmodifiableObservableList(approvedSellers);
    }

    @Override
    public void updateFromBlockchain(Blockchain blockchain) {
        pendingSellers.clear();
        approvedSellers.clear();
        // O(n^2) worst case. Could improve with map but this isn't an algorithms course
        for (Block sellerBlock : blockchain.getBlocks()) {
            if (sellerBlock.getData() instanceof Seller) {
                Seller seller = (Seller) sellerBlock.getData();
                boolean foundDecision = false;
                // Try work out the approved/deny status of this seller
                for (Block decisionBlock : blockchain.getBlocks()) {
                    if (decisionBlock.getData() instanceof EntityDecision){
                        EntityDecision decision= (EntityDecision) decisionBlock.getData();
                        // Found a decision for this block, lets see what it is
                        if(decision.getHashTarget().equals(sellerBlock.getHash())){
                            foundDecision=true;
                            if(decision.getStatus()==ApplicationStatus.APPROVED)
                                approvedSellers.add(seller);
                            // else we ignore denied sellers.
                        }
                    }
                }
                if (!foundDecision)
                    pendingSellers.add(seller);
            }
        }
    }
}
