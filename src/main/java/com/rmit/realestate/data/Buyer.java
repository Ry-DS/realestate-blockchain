package com.rmit.realestate.data;

import java.util.Date;

public class Buyer {
    private final String fullName;
    private final Date dob;
    private final String currentAddress;
    private final String contactNumber;
    private final String employerName;
    private final int loanAmount;
    private final String propertyAddress;

    public Buyer(String fullName, Date dob, String currentAddress, String contactNumber, String employerName, int loanAmount, String propertyAddress) {
        this.fullName = fullName;
        this.dob = dob;
        this.currentAddress = currentAddress;
        this.contactNumber = contactNumber;
        this.employerName = employerName;
        this.loanAmount = loanAmount;
        this.propertyAddress = propertyAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public Date getDob() {
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

    public int getLoanAmount() {
        return loanAmount;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }
}
