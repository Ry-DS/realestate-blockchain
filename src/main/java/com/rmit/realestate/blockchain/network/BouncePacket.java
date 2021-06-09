package com.rmit.realestate.blockchain.network;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A packet that carries some data and a bounce count.
 * Until the bounce count equals 0, this packet should be resent on the network.
 * Once a client receives a bounce packet with 0 bounces, it should disregard it.
 * IMMUTABLE, final is not supported in serialized classes
 */
public class BouncePacket<T> {
    private Set<Integer> viewed = new HashSet<>();
    T data;

    public BouncePacket(T data, int selfPort) {
        this.data = data;
        viewed.add(selfPort);
    }

    // For serialization
    private BouncePacket() {
    }

    public T getData() {
        return data;
    }

    public void addPort(int port) {
        viewed.add(port);
    }

    public Collection<Integer> getViewed() {
        return viewed;
    }
}
