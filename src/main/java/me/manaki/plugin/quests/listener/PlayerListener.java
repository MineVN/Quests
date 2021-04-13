package me.manaki.plugin.quests.listener;

import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quester.Questers;
import mk.plugin.santory.utils.Tasks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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

}
