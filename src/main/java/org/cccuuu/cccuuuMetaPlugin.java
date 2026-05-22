package org.cccuuu;

import org.cccuuu.listeners.AutoLoginListener;
import org.cccuuu.listeners.ClientListenerWrapper;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import xin.bbtt.mcbot.Bot;
import xin.bbtt.mcbot.Server;
import xin.bbtt.mcbot.plugin.MetaPlugin;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class cccuuuMetaPlugin implements MetaPlugin {
    @Override
    public void onLoad() {
        // LangManager.initLang(cccuuuMetaPlugin.class.getClassLoader());
    }

    @Override
    public void onUnload() {
    }

    @Override
    public void onEnable() {
        // Packet listeners
        Bot.INSTANCE.addPacketListener(new ClientListenerWrapper(), this);
        Bot.INSTANCE.addPacketListener(new AutoLoginListener(), this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public SocketAddress getServerSocketAddress() {
        return new InetSocketAddress("3c3u.org", 25565);
    }

    @Override
    public Server getServer(ClientboundLoginPacket loginPacket) {
        if (loginPacket.getCommonPlayerSpawnInfo().getGameMode() == GameMode.ADVENTURE) {
            return Server.Login;
        }
        return Server.Game;
    }
}
