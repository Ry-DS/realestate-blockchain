package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.BlockData;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.Hashing;
import com.rmit.realestate.blockchain.SecurityEntity;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class Buyer extends BlockPointer {
    private String fullName;
    private long dob;
    private String currentAddress;
    private String contactNumber;
    private String employerName;
    private int loanAmount;
    private int loanApplicationId = -1;

    public Buyer(String fullName, long dob, String currentAddress, String contactNumber, String employerName, int loanAmount, Block sellerBlock) {
        super(sellerBlock);
        this.loanAmount = loanAmount;
        this.fullName = fullName;
        this.dob = dob;
        this.currentAddress = currentAddress;
        this.contactNumber = contactNumber;
        this.employerName = employerName;
    }

    // For serialization
    private Buyer() {

    }

    public String getPropertyAddress(Blockchain blockchain) {
        BlockData data = getBlockPointer(blockchain).getData();
        if (!(data instanceof Seller))
            return null;
        return ((Seller) data).getPropertyAddress();
    }

    @Override
    public String toString() {
        return getFullName();
    }


    @Override
    public boolean verify(Blockchain blockchain, Block container) {
        if (fullName == null || dob >= new Date().getTime() || currentAddress == null || contactNumber == null
                || employerName == null || loanAmount <= 0 || loanApplicationId <= 0
                || getHashTarget() == null || container.getCreator() != SecurityEntity.BUYER)
            return false;
        Block sellerBlock = getBlockPointer(blockchain);
        if (sellerBlock == null || !(sellerBlock.getData() instanceof Seller))
            return false;
        // Used to get approved and pending buyers.
        BuyerDao buyerDao = new BuyerDao();
        buyerDao.updateFromBlockchain(blockchain);
        // If this buyer is in the middle of a sale, or completed a sale,
        // make sure no other buyer is also on the same sale.
        if (buyerDao.containsBuyer(this)) {
            return Stream.of(buyerDao.getApprovedBuyers(), buyerDao.getPendingBuyers()).flatMap(List::stream)
                    .noneMatch(b -> b.getHashTarget().equals(getHashTarget()) && b != this);
        }
        return true;
    }

    @Override
    public String hash() {
        return Hashing.hash(super.hash(), fullName, dob, currentAddress, contactNumber, employerName, loanAmount);
    }

    public int getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(int loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public String getFullName() {
        return fullName;
    }

    // TODO Change String dob back to Date dob
    public long getDob() {
        return dob;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmployerName() {
        return employerName;
    }


}
