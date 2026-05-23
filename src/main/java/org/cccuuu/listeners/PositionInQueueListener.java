package org.cccuuu.listeners;

import org.cccuuu.events.PlayingPlayersUpdateEvent;
import org.cccuuu.events.PositionInQueueUpdateEvent;
import org.geysermc.mcprotocollib.network.Session;
import org.geysermc.mcprotocollib.network.event.session.SessionAdapter;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.title.ClientboundSetSubtitleTextPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xin.bbtt.mcbot.Bot;
import xin.bbtt.mcbot.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PositionInQueueListener extends SessionAdapter {
    private static final Logger log = LoggerFactory.getLogger(PositionInQueueListener.class.getSimpleName());
    private static final String regex = "^(\\d+)\\s+§lPlaying\\s+\\|\\s+§lPosition in queue:\\s+(\\d+)$";
    private static final Pattern pattern = Pattern.compile(regex);
    private int lastPlayingPlayers = 0;
    private int lastPositionInQueue = 0;
    @Override
    public void packetReceived(Session session, Packet packet) {
        if (!(packet instanceof ClientboundSetSubtitleTextPacket setSubtitleTextPacket)) return;
        String text = Utils.toString(setSubtitleTextPacket.getText());
        Matcher matcher = pattern.matcher(text);
        if (!matcher.matches()) return;
        int playingPlayers = Integer.parseInt(matcher.group(1));
        int positionInQueue = Integer.parseInt(matcher.group(2));
        if (playingPlayers != lastPlayingPlayers) {
            PlayingPlayersUpdateEvent playingPlayersUpdateEvent = new PlayingPlayersUpdateEvent(playingPlayers);
            Bot.INSTANCE.getPluginManager().events().callEvent(playingPlayersUpdateEvent);
        }
        if (positionInQueue != lastPositionInQueue) {
            PositionInQueueUpdateEvent positionInQueueUpdateEvent = new PositionInQueueUpdateEvent(positionInQueue);
            Bot.INSTANCE.getPluginManager().events().callEvent(positionInQueueUpdateEvent);
            log.info("Position in queue: {}", positionInQueue);
        }
        lastPlayingPlayers = playingPlayers;
        lastPositionInQueue = positionInQueue;
    }
}
