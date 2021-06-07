package com.rmit.realestate.blockchain;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PeerConnectionManager {
    // Servers only SEND data
    // Clients we connect to are for RECEIVING data only
    // From this we make a rudimentary P2P network (not scalable)
    private final int DEFAULT_PORT = 1000;
    private final int MIN_SEARCH_PORT = 1000;
    private final int MAX_SEARCH_PORT = MIN_SEARCH_PORT + 10;
    private final List<Client> peerReceivers = new ArrayList<>();
    private final Server peerBroadcaster = new Server();
    private final List<Consumer<Packet>> listeners = new ArrayList<>();

    public PeerConnectionManager() throws IOException {
        int port;
        String argPort = System.getenv("port");
        if (argPort == null || argPort.isEmpty())
            port = DEFAULT_PORT;
        else port = Integer.parseInt(argPort);

        System.out.println("Starting server with port: " + port);
        peerBroadcaster.start();
        try {
            peerBroadcaster.bind(port);
        } catch (IOException ex) {
            ex.printStackTrace();
            peerBroadcaster.stop();
            throw ex;
        }
        Thread peerFinder = new Thread(() -> {
            while (true) {
                for (int i = MIN_SEARCH_PORT; i <= MAX_SEARCH_PORT; i++) {
                    int finalI = i;
                    if (i == port || peerReceivers.stream().anyMatch(c -> c.getRemoteAddressTCP().getPort() == finalI))
                        continue;
                    Client client = new Client();
                    client.start();
                    try {
                        client.connect(1, "localhost", i);
                        System.out.println("Found valid connection on port: " + i);
                        peerReceivers.add(client);
                        client.addListener(new Listener() {
                            @Override
                            public void received(Connection connection, Object object) {
                                receivedMessage(object);
                            }

                            @Override
                            public void disconnected(Connection connection) {
                                peerReceivers.remove(client);
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


    }

    public void receivedMessage(Object obj) {
        if (!(obj instanceof Packet))
            return;
        listeners.forEach(listener -> {
            listener.accept((Packet) obj);
        });

    }

    public void broadcastMessage(Object msg) {
        peerBroadcaster.sendToAllTCP(msg);
    }
}
