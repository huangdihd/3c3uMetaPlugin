package org.cccuuu.listeners;

import lombok.Getter;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.ClientboundSystemChatPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.ServerboundChatCommandPacket;
import xin.bbtt.mcbot.Bot;
import xin.bbtt.mcbot.Utils;
import xin.bbtt.mcbot.events.LoginSuccessEvent;
import xin.bbtt.mcbot.events.SendLoginCommandEvent;
import xin.bbtt.mcbot.events.SendRegisterCommandEvent;


public class AutoLoginListener extends SessionAdapter {
    @Getter
    private static boolean login = false;

    @Override
    public void packetReceived(Session session, Packet packet) {
        if (packet instanceof ClientboundSystemChatPacket systemChatPacket) login(systemChatPacket);
        if (packet instanceof ClientboundSystemChatPacket systemChatPacket) register(systemChatPacket);
        if (packet instanceof ClientboundSystemChatPacket systemChatPacket) loginSuccessfully(systemChatPacket);
    }

    private void loginSuccessfully(ClientboundSystemChatPacket systemChatPacket) {
        if (!Utils.toString(systemChatPacket.getContent()).equals("§2§l成功登录!")) return;
        LoginSuccessEvent loginSuccessEvent = new LoginSuccessEvent();
        Bot.INSTANCE.getPluginManager().events().callEvent(loginSuccessEvent);
        login = true;
    }

    private void login(ClientboundSystemChatPacket systemChatPacket) {
        if (!Utils.toString(systemChatPacket.getContent()).equals("§c使用指令登录: /login <password>")) return;
        String loginCommand = "l " + Bot.INSTANCE.getConfig().getConfigData().getAccount().getPassword();
        SendLoginCommandEvent loginCommandEvent = new SendLoginCommandEvent(loginCommand);
        if (loginCommandEvent.isDefaultActionCancelled()) return;
        Bot.INSTANCE.getSession().send(new ServerboundChatCommandPacket(loginCommandEvent.getCommand()));
    }
    private void register(ClientboundSystemChatPacket systemChatPacket) {
        if (!Utils.toString(systemChatPacket.getContent()).equals("§3使用以下指令注册账号: /register <密码> <密码>")) return;
        String registerCommand = "reg " + Bot.INSTANCE.getConfig().getConfigData().getAccount().getPassword() + " " + Bot.INSTANCE.getConfig().getConfigData().getAccount().getPassword();
        SendRegisterCommandEvent registerCommandEvent = new SendRegisterCommandEvent(registerCommand);
        if (registerCommandEvent.isDefaultActionCancelled()) return;
        Bot.INSTANCE.getSession().send(new ServerboundChatCommandPacket(registerCommandEvent.getCommand()));
    }
}
