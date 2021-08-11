package me.manaki.plugin.quests.listener;

import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quest.stage.StageType;
import me.manaki.plugin.quests.quester.QuestData;
import me.manaki.plugin.quests.quester.Questers;
import me.manaki.plugin.quests.utils.Utils;
import mk.plugin.santory.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.Map;

public class PlayerListener implements Listener {

    private final Quests plugin;
    public PlayerListener(Quests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPvP(PlayerDeathEvent e) {
        var target = e.getEntity();
        var killer = target.getKiller();
        if (killer != null) {
            Map<String, Object> values = Map.of("player-name", target.getName());
            plugin.getQuestManager().addCount(killer, StageType.PLAYER_KILL, values, 1);
        }
    }

    @EventHandler
    public void onFishing(PlayerFishEvent e) {
        var p = e.getPlayer();
        var caught = e.getCaught();
        if (caught instanceof Item) {
            var type = ((Item) e.getCaught()).getItemStack().getType();
            Map<String, Object> values = Map.of("item-type", type.name());
            plugin.getQuestManager().addCount(p, StageType.FISHING, values, 1);
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e) {
        var p = e.getPlayer();
        var b = e.getBlock();

        Map<String, Object> values = Map.of("block-type", b.getType().name(), "world", b.getWorld().getName());
        plugin.getQuestManager().addCount(p, StageType.BLOCK_BREAK, values, 1);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        var player = e.getPlayer();
        // First join load
        Bukkit.getScheduler().runTaskLater(Quests.get(), () -> {
            if (!plugin.isLoaded()) {
                plugin.setLoaded(true);
                plugin.load();
            }
            Tasks.sync(() -> {
                plugin.getQuestManager().checkWrongQuests(player);
                plugin.getQuestManager().checkExpiredQuests(player);
            }, 50);
        }, 20);

        // Main quest check
        Bukkit.getScheduler().runTaskLater(Quests.get(), () -> {
            plugin.getQuestManager().checkMainQuest(player);
        }, 150);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        var player = e.getPlayer();
        Questers.saveAndClearCache(player.getName());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        var player = e.getPlayer();
        var quester = Questers.get(player.getName());
        for (Map.Entry<String, QuestData> entry : quester.getCurrentQuests().entrySet()) {
            var questID = entry.getKey();
            var stage = plugin.getQuestManager().getCurrentStage(player, questID);
            if (stage.getType() == StageType.LOCATION_REACH) {
                var locationData = stage.getData().get("location");
                var location = Utils.toLocation(locationData);
                if (player.getWorld() == location.getWorld()) {
                    Map<String, Object> map = Map.of("radius", player.getLocation().distance(location));
                    if (stage.match(map)) plugin.getQuestManager().addCount(player, questID, 1);
                }
            }
        }
    }

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent e) {
        var player = e.getPlayer();
        var cmd = e.getMessage().replace("/", "");
        plugin.getQuestManager().addCount(player, StageType.COMMAND_EXECUTE, Map.of("command", cmd), 1);
    }

}
