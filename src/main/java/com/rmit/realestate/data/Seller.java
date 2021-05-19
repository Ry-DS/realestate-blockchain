package com.rmit.realestate.data;

import java.io.File;

public class Seller {
    private final String propertyAddress;
    private final String ownerVendorFirstName;
    private final String ownerVendorLastName;
    private final File buildingDesign;
    private final int licenseNumber;

    public Seller(String propertyAddress, String ownerVendorFirstName, String ownerVendorLastName, File buildingDesign, int licenseNumber) {
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

    public int getLicenseNumber() {
        return licenseNumber;
    }





}






