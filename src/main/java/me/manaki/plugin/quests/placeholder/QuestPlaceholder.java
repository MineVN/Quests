package me.manaki.plugin.quests.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quester.Questers;
import me.manaki.plugin.quests.utils.Utils;
import org.bukkit.entity.Player;

import java.util.List;

public class QuestPlaceholder extends PlaceholderExpansion {


    @Override
    public String getIdentifier() {
        return "quests";
    }

    @Override
    public String getAuthor() {
        return "MankaiStep";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    // %mainquest_name%, %mainquest_stage%, %mainquest_objective_1%, %mainquest_objective_2%
    @Override
    public String onPlaceholderRequest(Player player, String s){
        String mainQuest = null;
        var quester = Questers.get(player.getName());
        for (String id : quester.getCurrentQuests().keySet()) {
            if (Quests.get().getConfigManager().getFeatherboardQuests().contains(id)) mainQuest = id;
        }
        if (mainQuest == null) return "Không có";

        var quest = Quests.get().getConfigManager().getQuest(mainQuest);
        var data = quester.getCurrentQuests().get(mainQuest);
        var stage = Quests.get().getQuestManager().getCurrentStage(player, mainQuest);
        var objective = stage.getObjective();
        var tip = stage.getTip();

        if (s.equalsIgnoreCase("main_quest_name")) {
            return quest.getName();
        }

        else if (s.equalsIgnoreCase("main_quest_stage")) {
            return (data.getStage() + 1) + "/" + quest.getStages().size();
        }

        else if (s.equalsIgnoreCase("main_quest_objective_1")) {
            List<String> os = Utils.toList(objective, 23, "");
            return os.get(0);
        }

        else if (s.equalsIgnoreCase("main_quest_objective_2")) {
            List<String> os = Utils.toList(objective, 23, "");
            return os.size() > 1 ? os.get(1) : "";
        }

        else if (s.equalsIgnoreCase("main_quest_objective_3")) {
            List<String> os = Utils.toList(objective, 23, "");
            return os.size() > 2 ? os.get(2) : "";
        }

        else if (s.equalsIgnoreCase("main_quest_tip_1")) {
            if (tip == null) {
                return "Nhiệm vụ này không có";
            }
            List<String> os = Utils.toList(tip, 23, "");
            return os.get(0);
        }

        else if (s.equalsIgnoreCase("main_quest_tip_2")) {
            if (tip == null) {
                return "gợi ý";
            }
            List<String> os = Utils.toList(tip, 23, "");
            return os.size() > 1 ? os.get(1) : "";
        }

        else if (s.equalsIgnoreCase("main_quest_tip_3")) {
            if (tip == null) {
                return "gợi ý";
            }
            List<String> os = Utils.toList(tip, 23, "");
            return os.size() > 2 ? os.get(2) : "";
        }

        return "Wrong placeholder";
    }

}
