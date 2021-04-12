package me.manaki.plugin.quests.listener;

import me.manaki.plugin.quests.quester.Questers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        var player = e.getPlayer();
        Questers.saveAndClearCache(player.getName());
    }

}
