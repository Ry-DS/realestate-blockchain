package org.example.data;

import javafx.scene.control.ComboBox;

import java.util.Date;

public class Buyer {
    private String fullName;
    private Date dob;
    private String currentAddress;
    private String contactNumber;
    private String employerName;
    private int loanAmount;
    private String propertyAddress;

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
