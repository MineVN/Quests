package me.manaki.plugin.quests.listener.hook;

import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quest.stage.StageType;
import mk.plugin.santory.event.PlayerItemAscentEvent;
import mk.plugin.santory.event.PlayerItemEnhanceEvent;
import mk.plugin.santory.event.PlayerItemUpgradeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class SantoryListener implements Listener {

    private final Quests plugin;

    public SantoryListener(Quests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnhance(PlayerItemEnhanceEvent e) {
        var player = e.getPlayer();
        int lv = e.getAfter();
        Map<String, Object> values = Map.of("enhance-level", lv);
        plugin.getQuestManager().addCount(player, StageType.SORA_ITEM_ENHANCE, values, 1);
    }

    @EventHandler
    public void onUpgrade(PlayerItemUpgradeEvent e) {
        var player = e.getPlayer();
        int lv = e.getAfter();
        Map<String, Object> values = Map.of("upgrade-level", lv);
        plugin.getQuestManager().addCount(player, StageType.SORA_ITEM_UPGRADE, values, 1);
    }

    @EventHandler
    public void onAscent(PlayerItemAscentEvent e) {
        var player = e.getPlayer();
        int lv = e.getAfter();
        Map<String, Object> values = Map.of("ascent-level", lv);
        plugin.getQuestManager().addCount(player, StageType.SORA_ITEM_ASCENT, values, 1);
    }

}
