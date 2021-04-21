package me.manaki.plugin.quests.listener;

import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quest.stage.StageType;
import me.manaki.plugin.quests.quester.QuestData;
import me.manaki.plugin.quests.quester.Questers;
import me.manaki.plugin.quests.utils.Utils;
import mk.plugin.santory.utils.Tasks;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Map;

public class PlayerListener implements Listener {

    private final Quests plugin;

    public PlayerListener(Quests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        var player = e.getPlayer();
        Tasks.async(() -> {
            plugin.getQuestManager().checkWrongQuests(player);
        }, 50);
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
