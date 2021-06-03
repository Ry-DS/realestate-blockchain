package com.rmit.realestate.blockchain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Blockchain {
    private final List<Block<Verifiable
            >> blocks = new ArrayList<>();

    public boolean verify() {
        for (int i = 1; i < blocks.size(); i++) {
            var currentBlock = blocks.get(i);
            var previousBlock = blocks.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.err.println("Hash of block " + i + " is incorrect.");

                return false;
            }
            if (!previousBlock.getHash().equals(currentBlock.getPrevHash())) {
                System.err.println("Previous hash doesn't match current block " + i);
                return false;
            }
            // Check if signatures are correct: Proof of Authority
            
            // if not signed by authority
            // if not signed by publisher

            if (!currentBlock.getData().verify(this)) {
                return false;
            }


        }

        return false; // This was missing when pulled from github, I have added this please make sure it was required.
                      // Without The app doesn't run and error comes up
    }

    public Collection<Block<Verifiable>> getBlocks() {
        return Collections.unmodifiableCollection(blocks);
    }
}
