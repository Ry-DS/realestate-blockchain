package com.rmit.realestate.data;

import com.rmit.realestate.blockchain.Block;
import com.rmit.realestate.blockchain.BlockData;
import com.rmit.realestate.blockchain.Blockchain;
import com.rmit.realestate.blockchain.Hashing;
import com.rmit.realestate.blockchain.SecurityEntity;

import java.io.File;

public class Seller implements BlockData {
    private String propertyAddress;
    private String ownerVendorName;
    private String buildingDesign;
    private String licenceNumber;
    private int permitId = -1;

    public Seller(String propertyAddress, String ownerVendorName, String licenceNumber, File buildingDesign) {
        this.propertyAddress = propertyAddress;
        this.ownerVendorName = ownerVendorName;
        this.licenceNumber = licenceNumber;
        // TODO save file properly
        this.buildingDesign = buildingDesign.getName();

    }
    // For serialization
    private Seller(){}

    public String getLicenceNumber() {
        return licenceNumber;
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

    public int getPermitId() {
        return permitId;
    }

    public void setPermitId(int permitId) {
        this.permitId = permitId;
    }

    @Override
    public String toString() {
        return getPropertyAddress();
    }

    @Override
    public boolean verify(Blockchain blockchain, Block container) {
        // Check that only the seller made this block
        if (propertyAddress == null || ownerVendorName == null || buildingDesign == null
                || permitId < 0 || container.getCreator() != SecurityEntity.SELLER)
            return false;
        // Check if licence number already exists on blockchain.
        for (Block block : blockchain.getBlocks()) {
            if (block.getData() instanceof Seller) {
                Seller otherSeller = (Seller) block.getData();
                if (otherSeller.getPermitId() == permitId && block != container) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String hash() {
        return Hashing.hash(propertyAddress, ownerVendorName, buildingDesign, permitId);
    }
}






