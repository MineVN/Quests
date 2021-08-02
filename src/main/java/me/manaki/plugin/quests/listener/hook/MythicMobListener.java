package me.manaki.plugin.quests.listener.hook;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quest.stage.StageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class MythicMobListener implements Listener {

    private final Quests plugin;

    public MythicMobListener(Quests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMythicMobKill(MythicMobDeathEvent e) {
        var killer = e.getKiller();
        if (!(killer instanceof Player)) return;

        var p = (Player) killer;
        var id = e.getMobType().getInternalName();

        Map<String, Object> values = Map.of("mythicmob-id", id);

        plugin.getQuestManager().addCount(p, StageType.MYTHICMOB_KILL, values, 1);
    }

}
