package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.BlockData;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.Hashing;

import java.util.Date;

public class Buyer extends BlockPointer {
    private final String fullName;
    private final long dob;
    private final String currentAddress;
    private final String contactNumber;
    private final String employerName;
    private final int loanAmount;
    private int loanApplicationId;

    // TODO Change String dob back to Date dob in Constructor Parameter
    public Buyer(String fullName, long dob, String currentAddress, String contactNumber, String employerName, int loanAmount, Block sellerBlock) {
        super(sellerBlock);
        this.loanAmount = loanAmount;
        this.fullName = fullName;
        this.dob = dob;
        this.currentAddress = currentAddress;
        this.contactNumber = contactNumber;
        this.employerName = employerName;
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
        if (fullName == null || dob >= new Date().getTime() || currentAddress == null || contactNumber == null || employerName == null || loanAmount <= 0 || hashTarget == null)
            return false;
        String propertyAddress = getPropertyAddress(blockchain);
        if (propertyAddress == null)
            return false;
        // check existing buyer
        for (Block block : blockchain.getBlocks()) {
            if (block.getData() instanceof Buyer) {
                Buyer otherBuyer = (Buyer) block.getData();
                if (otherBuyer.hashTarget.equals(hashTarget) && block != container)
                    return false;
            }
        }
        return true;
    }

    @Override
    public String hash() {
        return Hashing.hash(super.hash(), fullName, dob, currentAddress, contactNumber, employerName, loanAmount);
    }
}
