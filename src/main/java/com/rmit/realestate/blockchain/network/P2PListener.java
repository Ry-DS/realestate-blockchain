package com.rmit.realestate.blockchain.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public abstract class P2PListener extends Listener {

    @Override
    public void connected(Connection connection) {
        onServerConnect(connection);
    }

    public abstract void onClientJoinAttempt(Client client);

    public abstract void onServerConnect(Connection connection);

    public abstract void onIncomingData(Object obj);
}
