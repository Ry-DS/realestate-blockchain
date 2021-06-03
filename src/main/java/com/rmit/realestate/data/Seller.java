package com.rmit.realestate.data;

import java.io.File;

public class Seller {
    private final String propertyAddress;
    private final String ownerVendorName;
    private final File buildingDesign;
    private final String licenseNumber;

    public Seller(String propertyAddress, String ownerVendorName, File buildingDesign, String licenseNumber) {
        this.propertyAddress = propertyAddress;
        this.ownerVendorName = ownerVendorName;
        this.buildingDesign = buildingDesign;
        this.licenseNumber = licenseNumber;

    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public String getOwnerVendorName() {
        return ownerVendorName;
    }

    public File getBuildingDesign() {
        return buildingDesign;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }





}






