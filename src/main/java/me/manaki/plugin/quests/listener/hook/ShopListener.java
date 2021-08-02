package me.manaki.plugin.quests.listener.hook;

import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quest.stage.StageType;
import me.manaki.plugin.quests.quester.Questers;
import me.manaki.plugin.shops.event.PlayerShopItemBuyEvent;
import me.manaki.plugin.shops.storage.ItemStorage;
import mk.plugin.santory.utils.Tasks;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class ShopListener implements Listener {

    private final Quests plugin;

    public ShopListener(Quests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBuy(PlayerShopItemBuyEvent e) {
        var p = e.getPlayer();
        var itemid = e.getItemID();

        Map<String, Object> values = Map.of("item-id", itemid);
        plugin.getQuestManager().addCount(p, StageType.SHOPS_ITEM_BUY, values, 1);
    }

    @EventHandler
    public void onDeliver(NPCRightClickEvent e) {
        var player = e.getClicker();
        var npcid = e.getNPC().getId();
        var is = player.getInventory().getItemInMainHand();
        if (is.getType() == Material.AIR) return;

        var itemid = ItemStorage.getID(is);
        if (itemid == null) return;

        Map<String, Object> values = Map.of("npc-id", npcid, "item-id", itemid);
        int excess = plugin.getQuestManager().addCount(player, StageType.SHOPS_ITEM_DELIVER, values, is.getAmount());
        // -1 = fail
        if (excess == -1) return;

        Tasks.async(() -> {
            Questers.save(player.getName());
        });
        is.setAmount(excess);
        player.updateInventory();

    }

}
