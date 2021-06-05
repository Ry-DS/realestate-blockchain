package com.rmit.realestate.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SellerDao {
    private static final List<Seller> sellers = new ArrayList<>();

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
    }
}
