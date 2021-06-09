package com.rmit.realestate.blockchain.network;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A packet that carries some data a list of visitors.
 * If a client receives a packet and its on the visitor list, it should disregard the packet.
 * A client should also update the visitor list with themselves before they pass along this packet to others.
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
        return Collections.unmodifiableSet(viewed);
    }
}
