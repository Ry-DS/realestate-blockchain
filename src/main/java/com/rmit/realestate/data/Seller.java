package com.rmit.realestate.data;

import java.io.File;

public class Seller {
    private final String propertyAddress;
    private final String ownerVendorFirstName;
    private final String ownerVendorLastName;
    private final File buildingDesign;
    private final String licenseNumber; // Changed int to String

    public Seller(String propertyAddress, String ownerVendorFirstName, String ownerVendorLastName, File buildingDesign, String licenseNumber) {// Changed int to String
        this.propertyAddress = propertyAddress;
        this.ownerVendorFirstName = ownerVendorFirstName;
        this.ownerVendorLastName = ownerVendorLastName;
        this.buildingDesign = buildingDesign;
        this.licenseNumber = licenseNumber;

    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public String getOwnerVendorFirstName() {
        return ownerVendorFirstName;
    }

    public String getOwnerVendorLastName() {
        return ownerVendorLastName;
    }

    public File getBuildingDesign() {
        return buildingDesign;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    } // Changed int to String





}






