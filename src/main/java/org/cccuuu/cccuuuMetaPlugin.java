package org.cccuuu;

import lombok.Getter;
import org.cccuuu.eventListeners.PrivateChatMessageListener;
import org.cccuuu.eventListeners.PublicChatMessageListener;
import org.cccuuu.listeners.*;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.GameMode;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundLoginPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xin.bbtt.mcbot.Bot;
import xin.bbtt.mcbot.LangManager;
import xin.bbtt.mcbot.Server;
import xin.bbtt.mcbot.Utils;
import xin.bbtt.mcbot.LoginFlow.LoginFlow;
import xin.bbtt.mcbot.plugin.MetaPlugin;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class cccuuuMetaPlugin implements MetaPlugin {
    private static final Logger log = LoggerFactory.getLogger(cccuuuMetaPlugin.class.getSimpleName());

    @Getter
    private static final LoginFlow loginFlow = LoginFlow.builder(Bot.INSTANCE::sendCommand)
            .eventManager(Bot.INSTANCE.getPluginManager().events())
            .templateExpander(t -> t.replace("{password}",
                    Bot.INSTANCE.getConfig().getConfigData().getAccount().getPassword()))
            .step(ClientboundSystemChatPacket.class)
                .match(p -> Utils.toString(p.getContent()).contains("§3使用以下指令注册账号: /register"))
                .then("reg {password} {password}")
                .register()
                .skipWhen(p -> Utils.toString(p.getContent()).contains("§c使用指令登录: /login"))
                .add()
            .step(ClientboundSystemChatPacket.class)
                .match(p -> Utils.toString(p.getContent()).contains("§c使用指令登录: /login"))
                .then("l {password}")
                .login()
                .successWhen(p -> Utils.toString(p.getContent()).contains("§2§l成功登录!"))
                .onSuccess(p -> log.info(LangManager.get("cccuuu.login.successful")))
                .add()
            .cooldown(2000)
            .build();

    @Override
    public void onLoad() {
        LangManager.initLang(cccuuuMetaPlugin.class.getClassLoader());
    }

    @Override
    public void onUnload() {
    }

    @Override
    public void onEnable() {
        loginFlow.reset();

        Bot.INSTANCE.addPacketListener(loginFlow, this);
        Bot.INSTANCE.addPacketListener(new ClientListenerWrapper(), this);
        Bot.INSTANCE.addPacketListener(new PositionInQueueListener(), this);
        Bot.INSTANCE.addPacketListener(new PingPacketListener(), this);
        Bot.INSTANCE.getPluginManager().events().registerEvents(new PublicChatMessageListener(), this);
        Bot.INSTANCE.getPluginManager().events().registerEvents(new PrivateChatMessageListener(), this);
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
            loginFlow.reset();
            return Server.Login;
        }
        return Server.Game;
    }
}
