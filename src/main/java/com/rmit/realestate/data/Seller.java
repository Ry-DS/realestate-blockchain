package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.BlockData;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.Hashing;
import com.rmit.realestate.blockchain.SecurityEntity;

import java.io.File;

public class Seller implements BlockData {
    private final String propertyAddress;
    private final String ownerVendorName;
    private final String buildingDesign;
    private int licenseNumber=-1;

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

    @Override
    public boolean verify(Blockchain blockchain, Block container) {
        // Check that only the seller made this block
        if (propertyAddress == null || ownerVendorName == null || buildingDesign == null
                || licenseNumber < 0 || container.getCreator() != SecurityEntity.SELLER)
            return false;
        // Check if licence number already exists on blockchain.
        for (Block block : blockchain.getBlocks()) {
            if (block.getData() instanceof Seller) {
                Seller otherSeller = (Seller) block.getData();
                if (otherSeller.getLicenseNumber() == licenseNumber && block != container) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String hash() {
        return Hashing.hash(propertyAddress, ownerVendorName, buildingDesign, licenseNumber);
    }
}






