package me.manaki.plugin.quests.listener.hook;

import me.manaki.plugin.market.event.PlayerMarketSellEvent;
import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quest.stage.StageType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class MarketListener implements Listener {

    private final Quests plugin;

    public MarketListener(Quests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSell(PlayerMarketSellEvent e) {
        var p = e.getPlayer();
        int amount = e.getAmount();
        var is = e.getItemStack();

        Map<String, Object> values = Map.of("item-material", is.getType().name());

        plugin.getQuestManager().addCount(p, StageType.MARKET_SELL, values, amount);
    }

}
