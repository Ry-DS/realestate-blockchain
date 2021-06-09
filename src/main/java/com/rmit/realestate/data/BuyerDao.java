package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.SecurityEntity;
import com.rmit.realestate.ui.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// TODO link with blockchain
public class BuyerDao implements BlockchainDao {
    private final ObservableList<Buyer> pendingBuyers = FXCollections.observableArrayList();
    private final ObservableList<Buyer> approvedBuyers = FXCollections.observableArrayList();

    /**
     * Adds a seller and returns the permit id.
     */
    public boolean addBuyer(Buyer buyer, SecurityEntity entity) {
        int id = App.getBlockchain().getBlocks().size();
        buyer.setLoanApplicationId(id);

        return App.publishBlock(buyer, entity);
    }

    public ObservableList<Buyer> getPendingBuyers() {
        return FXCollections.unmodifiableObservableList(pendingBuyers);
    }

    public ObservableList<Buyer> getApprovedBuyers() {
        return FXCollections.unmodifiableObservableList(approvedBuyers);
    }

    public boolean approve(Buyer buyer, SecurityEntity entity) {
        EntityDecision decision = new EntityDecision(App.getBlockchain().findBlockWithData(buyer), ApplicationStatus.APPROVED);
        return App.publishBlock(decision, entity);
    }

    public boolean disapprove(Buyer buyer, SecurityEntity entity) {
        EntityDecision decision = new EntityDecision(App.getBlockchain().findBlockWithData(buyer), ApplicationStatus.DENIED);
        return App.publishBlock(decision, entity);

    }

    // Populate this DAO from a blockchain. DO NOT use static App instance here, since this is called for verification
    @Override
    public void updateFromBlockchain(Blockchain blockchain) {
        pendingBuyers.clear();
        for (Block buyerBlock : blockchain.getBlocks()) {
            if (buyerBlock.getData() instanceof Buyer) {
                Buyer buyer = (Buyer) buyerBlock.getData();
                // Check if there's a decision for this  buyer. We don't store them if there's a decision.
                ApplicationStatus status = blockchain.getBlocks().stream().filter(decisionBlock ->
                        decisionBlock.getData() instanceof EntityDecision && ((EntityDecision) decisionBlock.getData()).getHashTarget().equals(buyerBlock.getHash()))
                        .map(b -> ((EntityDecision) b.getData()).getStatus()).findFirst().orElse(null);
                if (status == ApplicationStatus.APPROVED)
                    approvedBuyers.add(buyer);
                else if (status == null)
                    pendingBuyers.add(buyer);
            }
        }
    }

    public boolean containsBuyer(Buyer buyer) {
        return approvedBuyers.contains(buyer) || pendingBuyers.contains(buyer);
    }
}
