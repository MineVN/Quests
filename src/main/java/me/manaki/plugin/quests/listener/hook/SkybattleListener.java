package me.manaki.plugin.quests.listener.hook;

import manaki.plugin.skybattleclient.event.PlayerEndGameEvent;
import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quest.stage.StageType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class SkybattleListener implements Listener {

    private final Quests plugin;

    public SkybattleListener(Quests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSkybattleEnd(PlayerEndGameEvent e) {
        var p = e.getPlayer();
        int top = e.getResult().getTop();

        Map<String, Object> values = Map.of("top", top);

        plugin.getQuestManager().addCount(p, StageType.SKYBATTLE, values, 1);
    }

}
