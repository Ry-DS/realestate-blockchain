package com.rmit.blockchain.data;

import java.io.File;

public class Seller {
    private String propertyAddress;
    private String ownerVendorFirstName;
    private String ownerVendorLastName;
    private File buildingDesign;
    private int licenseNumber;

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






