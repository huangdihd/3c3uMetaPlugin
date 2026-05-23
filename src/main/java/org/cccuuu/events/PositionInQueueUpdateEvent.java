package org.cccuuu.events;

import lombok.Getter;
import xin.bbtt.mcbot.event.Event;
import xin.bbtt.mcbot.event.HandlerList;

public class PositionInQueueUpdateEvent extends Event {
    private final static HandlerList HANDLERS = new HandlerList();
    @Getter
    private final int positionInQueue;

    public PositionInQueueUpdateEvent(int positionInQueue) {
        this.positionInQueue = positionInQueue;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
