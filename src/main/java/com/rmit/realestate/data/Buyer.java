package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.BlockData;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.Hashing;

public class Buyer implements BlockData {
    private final String fullName;
    private final String dob;
    private final String currentAddress;
    private final String contactNumber;
    private final String employerName;
    private int loanAmount;
    private final String propertyAddress;

    // TODO Change String dob back to Date dob in Constructor Parameter
    public Buyer(String fullName, String dob, String currentAddress, String contactNumber, String employerName, String propertyAddress) {
        this.fullName = fullName;
        this.dob = dob;
        this.currentAddress = currentAddress;
        this.contactNumber = contactNumber;
        this.employerName = employerName;
        this.propertyAddress = propertyAddress;
    }

    public String getFullName() {
        return fullName;
    }

    // TODO Change String dob back to Date dob
    public String getDob() {
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


    public void setLoanAmount(int loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    @Override
    public String toString() {
        return getFullName();
    }


    @Override
    public boolean verify(Blockchain blockchain, Block container) {
        // Check DOB happens in the past.
        // Check contact-number has no letters.
        // Check nothing is null
        // Check name has no numbers
        // Size constraints
        // Block pointer to the seller's house
        return false;
    }

    @Override
    public String hash() {
        return Hashing.hash(fullName, dob, currentAddress, contactNumber, employerName, loanAmount, propertyAddress);
    }
}
