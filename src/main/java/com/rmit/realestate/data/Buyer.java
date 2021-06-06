package com.rmit.realestate.data;

import java.util.Date;

public class Buyer {
    private final String fullName;
    private final String dob; // TODO Change String dob back to Date dob, Wasn't sure how to make Date dob in BuyerController for Form so I changed this Temporary for you to fix
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


}
