package me.manaki.plugin.quests.listener.hook;

import me.manaki.plugin.crafts.event.ItemCraftEvent;
import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quest.stage.StageType;
import me.manaki.plugin.quests.quester.Questers;
import mk.plugin.santory.utils.Tasks;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class CraftListener implements Listener {

    private final Quests plugin;

    public CraftListener(Quests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemCraft(ItemCraftEvent e) {
        var player = e.getPlayer();
        var recipeID = e.getRecipeID();

        Map<String, Object> values = Map.of("recipe-id", recipeID);
        plugin.getQuestManager().addCount(player, StageType.SORA_ITEM_CRAFT, values, 1);
        Tasks.async(() -> {
            Questers.save(player.getName());
        });
    }

}
