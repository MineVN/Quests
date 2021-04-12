package me.manaki.plugin.quests.listener;

import me.manaki.plugin.quests.Quests;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

    private final Quests plugin;

    public GUIListener(Quests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        plugin.getGUIManager().onClick(e);
    }

}
