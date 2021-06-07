package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.SecurityEntity;
import com.rmit.realestate.ui.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO link with blockchain
public class BuyerDao implements BlockchainDao {
    private final ObservableList<Buyer> buyers = FXCollections.observableArrayList();

    /**
     * Adds a seller and returns the permit id.
     */
    public boolean addBuyer(Buyer buyer, SecurityEntity entity) {
        int id = App.getBlockchain().getBlocks().size();
        buyer.setLoanApplicationId(id);

        return App.publishBlock(buyer, entity);
    }

    public ObservableList<Buyer> getBuyers() {
        return FXCollections.unmodifiableObservableList(buyers);
    }

    public boolean approve(Buyer buyer, SecurityEntity entity) {
        EntityDecision decision = new EntityDecision(App.getBlockchain().findBlockWithData(buyer), ApplicationStatus.APPROVED);
        return App.publishBlock(decision, entity);
    }

    public boolean disapprove(Buyer buyer, SecurityEntity entity) {
        EntityDecision decision = new EntityDecision(App.getBlockchain().findBlockWithData(buyer), ApplicationStatus.DENIED);
        return App.publishBlock(decision, entity);

    }

    // TODO
    @Override
    public void updateFromBlockchain(Blockchain blockchain) {
        buyers.clear();
        for (Block buyerBlock : blockchain.getBlocks()) {
            if (buyerBlock.getData() instanceof Buyer) {
                Buyer buyer = (Buyer) buyerBlock.getData();
                // Check if there's a decision for this  buyer. We don't store them if there's a decision.
                boolean foundDecisionBlock = blockchain.getBlocks().stream().anyMatch(decisionBlock ->
                        decisionBlock.getData() instanceof EntityDecision && ((EntityDecision) decisionBlock.getData()).getHashTarget().equals(buyerBlock.getHash()));
                if (!foundDecisionBlock)
                    buyers.add(buyer);
            }
        }
    }
}
