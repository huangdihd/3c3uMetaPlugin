package org.cccuuu.listeners;

import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.ConnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.DisconnectedEvent;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.event.session.SessionListener;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.ClientListener;
import org.geysermc.mcprotocollib.protocol.packet.login.clientbound.ClientboundHelloPacket;

import java.util.ArrayList;
import java.util.List;

public class ClientListenerWrapper extends SessionAdapter {
    @Override
    public void connected(ConnectedEvent event) {
        Session session = event.getSession();
        List<SessionListener> listeners = new ArrayList<>(session.getListeners());

        for (SessionListener originalListener : listeners) {
            if (originalListener instanceof ClientListener originClientListener) {

                session.removeListener(originalListener);

                session.addListener(new ClientListenerProxy(originClientListener));
            }
        }
    }
}

class ClientListenerProxy extends SessionAdapter {
    private final ClientListener originClientListener;

    public ClientListenerProxy(ClientListener originClientListener) {
        this.originClientListener = originClientListener;
    }

    @Override
    public void packetReceived(Session s, Packet packet) {
        if (packet instanceof ClientboundHelloPacket helloPacket) {
            if (helloPacket.isShouldAuthenticate()) {

                ClientboundHelloPacket modifiedHello = new ClientboundHelloPacket(
                        helloPacket.getServerId(),
                        helloPacket.getPublicKey(),
                        helloPacket.getChallenge(),
                        false
                );
                originClientListener.packetReceived(s, modifiedHello);
                return;
            }
        }
        originClientListener.packetReceived(s, packet);
    }

    @Override
    public void disconnected(DisconnectedEvent e) {
        originClientListener.disconnected(e);
    }
}
