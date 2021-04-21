package me.manaki.plugin.quests.listener.hook;

import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quest.stage.StageType;
import me.manaki.plugin.quests.quester.QuestData;
import me.manaki.plugin.quests.quester.Questers;
import mk.plugin.santory.utils.Tasks;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;

public class ConversationListener implements Listener {

    private final Quests plugin;

    public ConversationListener(Quests plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeliver(NPCRightClickEvent e) {
        var player = e.getClicker();
        var npcid = e.getNPC().getId();

        // Talk
        var quester = Questers.get(player.getName());
        for (Map.Entry<String, QuestData> entry : quester.getCurrentQuests().entrySet()) {
            var id = entry.getKey();
            var data = entry.getValue();
            var stage = plugin.getQuestManager().getCurrentStage(player, id);
            if (stage.getType() != StageType.CONVERSATION) continue;

            var cid = "c-" + data.getStageCount();
            if (stage.getData().containsKey(cid)) {
                var message = stage.getData().get(cid).replace("&", "§").replace("%player%", player.getName());
                player.sendMessage("");
                player.sendMessage("§a§l[" + (data.getStageCount() + 1) + "/" + stage.getCount() + "] " + message);
                player.sendMessage("");
                player.playSound(player.getLocation(), Sound.BLOCK_WOODEN_BUTTON_CLICK_ON, 1, 1);
            }
        }
        
        // Add count
        Map<String, Object> values = Map.of("npc-id", npcid);
        int excess = plugin.getQuestManager().addCount(player, StageType.CONVERSATION, values, 1);
        if (excess == -1) return;

        // Get message
        Tasks.async(() -> {
            Questers.save(player.getName());
        });
    }

}
