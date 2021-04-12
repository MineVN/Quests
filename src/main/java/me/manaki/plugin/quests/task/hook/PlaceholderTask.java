package me.manaki.plugin.quests.task.hook;

import com.google.common.collect.Maps;
import me.clip.placeholderapi.PlaceholderAPI;
import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quest.stage.StageType;
import me.manaki.plugin.quests.quester.Quester;
import me.manaki.plugin.quests.quester.Questers;
import mk.plugin.playerdata.storage.PlayerData;
import mk.plugin.playerdata.storage.PlayerDataAPI;
import mk.plugin.santory.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class PlaceholderTask extends BukkitRunnable {

    public static final String KEY = "quest-placeholderchangestage-%placeholder%";

    private final Quests plugin;

    public PlaceholderTask(Quests plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            PlayerData pd = PlayerDataAPI.get(p, Questers.HOOK);
            Maps.newHashMap(pd.getDataMap()).forEach((k, v) -> {
                if (k.contains(KEY.replace("%placeholder%", ""))) {
                    String placeholder = k.replace(KEY.replace("%placeholder%", ""), "");
                    if (!placeholder.contains("%")) return;
                    double tv = Double.parseDouble(v);
                    double realv = Double.parseDouble(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(p.getUniqueId()), placeholder));

                    var quester = Questers.get(p.getName());
                    for (String questID : quester.getCurrentQuests().keySet()) {
                        var stage = plugin.getQuestManager().getCurrentStage(p, questID);
                        if (stage.getType() != StageType.PLACEHOLDER_CHANGE) continue;
                        if (!stage.getData().containsValue(placeholder)) continue;

                        double add = stage.getCount();
                        int currentCount = Double.valueOf(add - (tv - realv)).intValue();
                        if (currentCount == quester.getCurrentQuests().get(questID).getStageCount()) continue;

                        plugin.getQuestManager().setCount(p, questID, currentCount);
                        Tasks.async(() -> {
                            Questers.save(p.getName());
                        });
                    }
                }
            });
        });
    }

}
