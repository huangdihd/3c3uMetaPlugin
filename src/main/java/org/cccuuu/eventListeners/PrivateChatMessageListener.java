package org.cccuuu.eventListeners;

import org.geysermc.mcprotocollib.auth.GameProfile;
import xin.bbtt.mcbot.Bot;
import xin.bbtt.mcbot.event.EventHandler;
import xin.bbtt.mcbot.event.Listener;
import xin.bbtt.mcbot.events.PrivateChatEvent;
import xin.bbtt.mcbot.events.SystemChatMessageEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrivateChatMessageListener implements Listener {
    private static final Pattern PM_PATTERN = Pattern.compile("^📨\\s+(.+?)\\s+➡\\s+§d(.*)$");
    @EventHandler
    public void onChatMessage(SystemChatMessageEvent event) {
        String text =  event.getText();
        Matcher matcher = PM_PATTERN.matcher(text);
        if (!matcher.find()) return;
        String playerName = matcher.group(1);
        String message = matcher.group(2);
        for (GameProfile profile : Bot.INSTANCE.players.values()) {
            if (profile.getName().equals(playerName)) {
                PrivateChatEvent privateChatEvent = new PrivateChatEvent(profile, message);
                Bot.INSTANCE.getPluginManager().events().callEvent(privateChatEvent);
                break;
            }
        }
    }
}
