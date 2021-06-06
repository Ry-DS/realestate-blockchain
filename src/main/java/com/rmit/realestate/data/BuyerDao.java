package com.rmit.realestate.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuyerDao {
    private static final List<Buyer> buyers = new ArrayList<>();

    /**
     * Adds a seller and returns the permit id.
     */
    public static int addBuyer(Buyer buyer) {
        // TODO
        buyers.add(buyer);
        int id = buyers.size();
        buyer.setLoanAmount(id);
        return id;
    }

    public static List<Buyer> getBuyers() {
        return Collections.unmodifiableList(buyers);
    }

    public static void approve(Buyer buyer) {
        // TODO
        buyers.remove(buyer);
    }

    public static void disapprove(Buyer buyer) {
        // TODO
        buyers.remove(buyer);
    }
}
