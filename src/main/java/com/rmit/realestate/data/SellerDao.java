package com.rmit.realestate.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SellerDao {
    private static final List<Seller> sellers = new ArrayList<>();
    private static final List<Seller> sellers1 = new ArrayList<>();
    /**
     * Adds a seller and returns the permit id.
     */
    public static int addSeller(Seller seller) {
        // TODO
        sellers.add(seller);
        int id = sellers.size();
        seller.setLicenseNumber(id);
        return id;
    }

    public static List<Seller> getSellers() {
        return Collections.unmodifiableList(sellers);
    }


    public static void approve(Seller seller) {
        // TODO
        sellers.remove(seller);
        sellers1.add(seller);
        getApproved();


    }

    public static void disapprove(Seller seller) {
        // TODO
        sellers.remove(seller);
    }

    public static List<Seller> getApproved() {

        return Collections.unmodifiableList(sellers1);
    }
}
