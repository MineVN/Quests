package me.manaki.plugin.quests.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class QuestTakeEvent extends PlayerEvent {

    private final String questId;

    public QuestTakeEvent(@NotNull Player who, String questId) {
        super(who);
        this.questId = questId;
    }

    public String getQuestId() {
        return questId;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
