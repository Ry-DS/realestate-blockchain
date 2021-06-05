package com.rmit.realestate.data;

import java.io.File;

public class Seller {
    private final String propertyAddress;
    private final String ownerVendorName;
    private final String buildingDesign;
    private int licenseNumber;

    public Seller(String propertyAddress, String ownerVendorName, File buildingDesign) {
        this.propertyAddress = propertyAddress;
        this.ownerVendorName = ownerVendorName;
        // TODO save file properly
        this.buildingDesign = buildingDesign.getName();

    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public String getOwnerVendorName() {
        return ownerVendorName;
    }

    public String getBuildingDesign() {
        return buildingDesign;
    }

    public int getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(int licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    @Override
    public String toString() {
        return getPropertyAddress();
    }
}






