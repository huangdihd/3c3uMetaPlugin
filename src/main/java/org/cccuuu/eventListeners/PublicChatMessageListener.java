package org.cccuuu.eventListeners;

import org.geysermc.mcprotocollib.auth.GameProfile;
import xin.bbtt.mcbot.Bot;
import xin.bbtt.mcbot.event.EventHandler;
import xin.bbtt.mcbot.event.Listener;
import xin.bbtt.mcbot.events.PublicChatEvent;
import xin.bbtt.mcbot.events.SystemChatMessageEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PublicChatMessageListener implements Listener {
    private static final Pattern CHAT_PATTERN = Pattern.compile("^«([^»]+)»\\s*(.*)$");
    @EventHandler
    public void onChatMessage(SystemChatMessageEvent event) {
        String text =  event.getText();
        Matcher matcher = CHAT_PATTERN.matcher(text);
        if (!matcher.find()) return;
        String playerName = matcher.group(1);
        String message = matcher.group(2);
        if (message.startsWith("§a")) {
            message = message.substring(2);
        }
        for (GameProfile profile : Bot.INSTANCE.players.values()) {
            if (profile.getName().equals(playerName)) {
                PublicChatEvent publicChatEvent = new PublicChatEvent(profile, message);
                Bot.INSTANCE.getPluginManager().events().callEvent(publicChatEvent);
                break;
            }
        }
    }
}
