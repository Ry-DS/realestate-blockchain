package com.rmit.realestate.blockchain.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import org.bouncycastle.util.Arrays;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class PeerConnectionManager {
    // Servers only SEND data
    // Clients we connect to are for RECEIVING data only
    // From this we make a rudimentary P2P network (not scalable)
    public static final int PORT_SEARCH_RANGE = 10;
    private static final int BUFFER_SIZE = 10000000;
    private final List<Client> peerReceivers = new ArrayList<>();
    private final Server peerBroadcaster = new Server();
    private final List<P2PListener> listeners = new ArrayList<>();
    // To show in UI
    private final IntegerProperty numOfServerConnections = new SimpleIntegerProperty(0);
    private final IntegerProperty numOfClientConnections = new SimpleIntegerProperty(0);
    private final int port;
    private final int p2pPort;


    public PeerConnectionManager(int port, int p2pPort) throws IOException {
        this.port = port;
        this.p2pPort = p2pPort;
        System.out.println("Assigning server to port: " + port);
        try {
            peerBroadcaster.bind(port);
        } catch (IOException ex) {
            ex.printStackTrace();
            peerBroadcaster.stop();
            throw ex;
        }


    }

    private void trackNumberOfServerConnections() {
        peerBroadcaster.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                Platform.runLater(() -> numOfServerConnections.set(peerBroadcaster.getConnections().length));
            }

            @Override
            public void disconnected(Connection connection) {
                Platform.runLater(() -> numOfServerConnections.set(peerBroadcaster.getConnections().length));
            }
        });
    }

    private void startPeerFinderThread(int port, int p2pPort) {
        // Handles connecting to new clients to recieve data from.
        Thread peerFinder = new Thread(() -> {
            Object threadLock = new Object();
            while (true) {
                boolean freePort = false;
                for (int i = port - 1; i <= port + 1; i++) {
                    int finalI = i;
                    // True if there is still a port we need to try connect to.

                    // if port is self, or we already have a connection to this port...
                    if (i == port || peerReceivers
                            .stream()
                            .anyMatch(c -> c.getRemoteAddressTCP() != null && c.getRemoteAddressTCP()
                                    .getPort() == finalI))
                        continue;
                    // There's a port with no connection still
                    freePort = true;
                    Client client = new Client(BUFFER_SIZE, BUFFER_SIZE);
                    client.start();
                    listeners.forEach(l -> l.onClientJoinAttempt(client));
                    try {
                        client.connect(1, "localhost", i);
                        System.out.println("Found valid connection on port: " + i);
                        peerReceivers.add(client);
                        Platform.runLater(() -> numOfClientConnections.set(peerReceivers.size()));

                        client.addListener(new Listener() {
                            @Override
                            public void received(Connection connection, Object object) {
                                receivedMessage(object, connection.getRemoteAddressTCP());
                            }

                            @Override
                            public void disconnected(Connection connection) {
                                peerReceivers.remove(client);
                                Platform.runLater(() -> numOfClientConnections.set(peerReceivers.size()));
                                client.stop();
                                synchronized (threadLock) {
                                    // Wake up, there is a chance you can connect to new peers
                                    threadLock.notify();
                                }
                            }
                        });
                    } catch (IOException ex) {
                        // clean up client and ignore
                        client.stop();
                    }
                }
                if (!freePort) {
                    try {
                        System.out.println("Connected to Max Peers, waiting for a disconnect.");
                        synchronized (threadLock) {
                            threadLock.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
        peerFinder.setDaemon(true);
        peerFinder.start();
        peerFinder.setName("Peer Finder");
    }

    void receivedMessage(Object obj, InetSocketAddress conn) {
        listeners.forEach(listener -> listener.onIncomingData(obj, conn));
    }

    public void addNetworkListener(P2PListener listener) {
        peerBroadcaster.addListener(listener);
        listeners.add(listener);
    }

    public boolean removeNetworkListener(P2PListener listener) {
        peerBroadcaster.removeListener(listener);
        return listeners.remove(listener);
    }

    public ObservableIntegerValue numberOfServerConnectionsProperty() {
        return numOfServerConnections;
    }

    public ObservableIntegerValue numOfClientConnectionsProperty() {
        return numOfClientConnections;
    }

    public void broadcastMessage(Object msg) {
        peerBroadcaster.sendToAllTCP(msg);
    }

    Server getServer() {
        return peerBroadcaster;
    }

    public void startThreads() {
        Thread serverThread = new Thread(peerBroadcaster, "P2P Server");
        serverThread.setDaemon(true);
        serverThread.start();
        startPeerFinderThread(port, p2pPort);
        trackNumberOfServerConnections();

    }

    public int getPort() {
        return port;
    }
}
