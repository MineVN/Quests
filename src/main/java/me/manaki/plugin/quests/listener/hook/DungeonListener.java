package me.manaki.plugin.quests.listener.hook;

import me.manaki.plugin.dungeons.dungeon.event.DungeonFinishEvent;
import me.manaki.plugin.dungeons.dungeon.event.DungeonMobKilledEvent;
import me.manaki.plugin.dungeons.dungeon.status.DungeonResult;
import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quest.stage.StageType;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DungeonListener implements Listener {

    private final Quests plugin;

    public DungeonListener(Quests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFinish(DungeonFinishEvent e) {
        var dungeonID = e.getID();
        var result = e.getResult().name().toLowerCase();
        var memStart = e.getStatus().getStarters().size();
        var memFinish = e.getStatus().getPlayers().size();

        List<UUID> uuids = e.getResult() == DungeonResult.WIN ? e.getStatus().getPlayers() : e.getStatus().getStarters();

        for (UUID uuid : uuids) {
            var player = Bukkit.getPlayer(uuid);
            var statistic = e.getStatus().getStatistic(player);
            var mobKills = statistic.getMobKilled();
            var slaveSaves = statistic.getSlaveSaved();
            var deathTimes = statistic.getDead();
            var playTime = statistic.getTimeSurvived();

            Map<String, Object> values = Map.of("dungeon-id", dungeonID,
                                                "result", result,
                                                "mem-start", memStart,
                                                "mem-finish", memFinish,
                                                "mob-kills", mobKills,
                                                "slave-saves", slaveSaves,
                                                "death-times", deathTimes,
                                                "play-time", playTime);
            assert player != null;
            plugin.getQuestManager().addCount(player, StageType.DUNGEON_FINISH, values, 1);
        }
    }

    @EventHandler
    public void onKill(DungeonMobKilledEvent e) {
        var player = e.getKiller();
        var dungeonID = e.getID();
        var mobID = e.getMobID();
        Map<String, Object> values = Map.of("dungeon-id", dungeonID, "mob-id", mobID);
        plugin.getQuestManager().addCount(player, StageType.DUNGEON_MOB_KILL, values, 1);
    }

}
