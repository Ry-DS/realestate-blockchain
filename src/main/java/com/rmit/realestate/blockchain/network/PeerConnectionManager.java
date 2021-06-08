package com.rmit.realestate.blockchain.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PeerConnectionManager {
    // Servers only SEND data
    // Clients we connect to are for RECEIVING data only
    // From this we make a rudimentary P2P network (not scalable)
    public static final int PORT_SEARCH_RANGE = 10;
    private final List<Client> peerReceivers = new ArrayList<>();
    private final Server peerBroadcaster = new Server();
    private final List<Consumer<Object>> listeners = new ArrayList<>();
    // To show in UI
    private final IntegerProperty numOfServerConnections = new SimpleIntegerProperty(0);
    private final IntegerProperty numOfClientConnections = new SimpleIntegerProperty(0);

    public PeerConnectionManager(int port, int p2pPort) throws IOException {
        System.out.println("Starting server with port: " + port);
        Thread serverThread = new Thread(peerBroadcaster, "P2P Network");
        serverThread.setDaemon(true);
        serverThread.start();
        try {
            peerBroadcaster.bind(port);
        } catch (IOException ex) {
            ex.printStackTrace();
            peerBroadcaster.stop();
            throw ex;
        }
        startPeerFinderThread(port, p2pPort);
        trackNumberOfServerConnections();


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
            while (true) {
                for (int i = p2pPort; i <= p2pPort + PORT_SEARCH_RANGE; i++) {
                    int finalI = i;
                    if (i == port || peerReceivers.stream().anyMatch(c -> c.getRemoteAddressTCP().getPort() == finalI))
                        continue;
                    Client client = new Client();
                    client.start();
                    try {
                        client.connect(1, "localhost", i);
                        System.out.println("Found valid connection on port: " + i);
                        peerReceivers.add(client);
                        Platform.runLater(() -> numOfClientConnections.set(peerReceivers.size()));

                        client.addListener(new Listener() {
                            @Override
                            public void received(Connection connection, Object object) {
                                receivedMessage(object);
                            }

                            @Override
                            public void disconnected(Connection connection) {
                                peerReceivers.remove(client);
                                Platform.runLater(() -> numOfClientConnections.set(peerReceivers.size()));
                                client.stop();
                            }
                        });
                    } catch (IOException ex) {
                        // ignore
                    }
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        peerFinder.setDaemon(true);
        peerFinder.start();
        peerFinder.setName("Peer Finder");
    }

    void receivedMessage(Object obj) {
        listeners.forEach(listener -> listener.accept(obj));
    }

    public void addNetworkListener(Consumer<Object> listener) {
        listeners.add(listener);
    }

    public boolean removeNetworkListener(Consumer<Object> listener) {
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

}
