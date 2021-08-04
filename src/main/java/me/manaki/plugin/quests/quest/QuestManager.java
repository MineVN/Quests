package me.manaki.plugin.quests.quest;

import com.google.common.collect.Maps;
import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quest.requirement.Requirement;
import me.manaki.plugin.quests.quest.stage.Stage;
import me.manaki.plugin.quests.quest.stage.StageType;
import me.manaki.plugin.quests.quester.QuestData;
import me.manaki.plugin.quests.quester.Questers;
import me.manaki.plugin.quests.utils.Utils;
import me.manaki.plugin.quests.utils.command.Command;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

public class QuestManager {

    private final Quests plugin;

    public QuestManager(Quests plugin) {
        this.plugin = plugin;
    }

    public Stage getCurrentStage(Player player, String questID) {
        var quester = Questers.get(player.getName());
        if (!quester.getCurrentQuests().containsKey(questID)) return null;
        var quest = plugin.getConfigManager().getQuest(questID);
        if (quest == null) {
            System.out.println("§cQuest id " + questID + " of " + player.getName() + " is null");
            return null;
        }
        int stageIndex = quester.getCurrentQuests().get(questID).getStage();
        return quest.getStages().get(stageIndex);
    }

    public void give(Player player, String questID, boolean override) {
        var quester = Questers.get(player.getName());
        var success = quester.addCurrentQuest(questID, override);
        if (!success) return;

        // Start commands
        for (Command cmd : plugin.getConfigManager().getOnQuestStartCommands()) cmd.execute(player, getPlaceholders(player, questID));
        var stage = getCurrentStage(player, questID);
        stage.onStart(player, getPlaceholders(player, questID));

        // Board
        if (plugin.getConfigManager().getFeatherboardQuests().contains(questID)) {
            plugin.getBoardManager().openBoard(player, -1, true);
        }

        // Bossbar
        plugin.getBoardManager().sendBossbar(player);
    }

    /*
    -1 -> Can't add
    0 -> Added but not excess
    0+ -> Has excess
     */

    public int addCountWithExcess(Player player, StageType type, Map<String, Object> values, int amount) {
        var quester = Questers.get(player.getName());
        for (Map.Entry<String, QuestData> e : quester.getCurrentQuests().entrySet()) {
            var questID = e.getKey();
            var stage = getCurrentStage(player, questID);
            if (stage.getType() == type && stage.match(values)) {
                return addCount(player, questID, amount);
            }
        }
        return -1;
    }

    public boolean addCount(Player player, StageType type, Map<String, Object> values, int amount) {
        boolean check = false;
        var quester = Questers.get(player.getName());
        for (Map.Entry<String, QuestData> e : quester.getCurrentQuests().entrySet()) {
            var questID = e.getKey();
            var stage = getCurrentStage(player, questID);
            if (stage.getType() == type && stage.match(values)) {
                addCount(player, questID, amount);
                check = true;
            }
        }
        return check;
    }

    public int addCount(Player player, String questID, int amount) {
        var quester = Questers.get(player.getName());
        var data = quester.getCurrentQuests().get(questID);
        return setCount(player, questID, data.getStageCount() + amount);
    }

    public int setCount(Player player, String questID, int amount) {
        var quester = Questers.get(player.getName());
        var stage = getCurrentStage(player, questID);
        var data = quester.getCurrentQuests().get(questID);

        // If added
        boolean added = amount > data.getStageCount();

        // Add
        var quest = plugin.getConfigManager().getQuest(questID);
        int excess = amount - stage.getCount();
        data.setStageCount(Math.min(stage.getCount(), amount));

        // Add command
        if (added) {
            stage.getOnCountAddedCommands().forEach(cmd -> cmd.execute(player, getPlaceholders(player, questID)));
        }

        // Stage done
        if (excess >= 0) {
            // Run end commands
            stage.onEnd(player, getPlaceholders(player, questID));

            // If last stage
            if (data.getStage() == quest.getStages().size() - 1) {
                finishQuest(player, questID);
                return excess;
            }

            // Set next stage
            data.setStage(data.getStage() + 1);
            data.setStageCount(0);
            stage = getCurrentStage(player, questID);
            var placeholders = getPlaceholders(player ,questID);
            placeholders.put("%old_stage%", data.getStage() + "");
            placeholders.put("%new_stage%", (data.getStage() + 1) + "");
            placeholders.put("%max_stage%", quest.getStages().size() + "");

            // Run start commands
            stage.onStart(player, placeholders);

            // Global commands
            for (Command cmd : plugin.getConfigManager().getOnStageChangeCommands()) {
                cmd.execute(player, placeholders);
            }

            // Bossbar
            plugin.getBoardManager().sendBossbar(player);
        }

        return Math.max(0, excess);
    }

    public void finishQuest(Player player, String id) {
        var quester = Questers.get(player.getName());
        var quest = plugin.getConfigManager().getQuest(id);

        // Commands
        for (Command cmd : plugin.getConfigManager().getOnQuestFinishCommands()) cmd.execute(player, getPlaceholders(player, id));

        // Rewards
        for (Command cmd : quest.getRewardCommands()) {
            cmd.execute(player, getPlaceholders(player, id));
        }

        // Data
        quester.addCompletedQuest(id);
        quester.getCurrentQuests().remove(id);
        quester.save();

        // Board
        plugin.getBoardManager().closeBoard(player, false);

        // Bossbar
        plugin.getBoardManager().removeBossbar(player);
    }

    public void cancelQuest(Player player, String id) {
        var quester = Questers.get(player.getName());

        // Commands
        for (Command cmd : plugin.getConfigManager().getOnQuestCancelCommands()) cmd.execute(player, getPlaceholders(player, id));

        // Run end stage commands
        var stage = getCurrentStage(player, id);
        stage.onEnd(player, getPlaceholders(player, id));

        // Data
        quester.getCurrentQuests().remove(id);
        quester.save();

        // Board
        plugin.getBoardManager().closeBoard(player, false);
    }

    /*
    Placeholder:
    - %quest_name%
    - %quest_desc%
    - %quest_points%
    - %data_stage_count%
    - %stage_objective%
    - %stage_tip%
    - %stage_count%
    - %stage_data_<data>% ex: %stage_data_npcid%
     */
    private Map<String, String> getPlaceholders(Player player, String questID) {
        var quest = plugin.getConfigManager().getQuest(questID);
        var quester = Questers.get(player.getName());
        var data = quester.getCurrentQuests().get(questID);
        Map<String, String> map = Maps.newHashMap();

        map.put("%player%", player.getName());

        if (quest != null) {
            map.put("%quest_name%", Objects.requireNonNullElse(quest.getName(), ""));
            map.put("%quest_desc%", Objects.requireNonNullElse(quest.getDesc(), ""));
            map.put("%quest_points%", quester.getQuestPoints() + "");
            if (data != null) {
                map.put("%data_stage_count%", data.getStageCount() + "");
                var stage = getCurrentStage(player, questID);
                if (stage != null) {
                    map.put("%stage_objective%", Objects.requireNonNullElse(stage.getObjective(), ""));
                    map.put("%stage_tip%", Objects.requireNonNullElse(stage.getTip(), ""));
                    map.put("%stage_count%", stage.getCount() + "");
                    for (Map.Entry<String, String> e : stage.getData().entrySet()) {
                        map.put("%stage_data_" + e.getKey().replace("-", "") + "%", e.getValue());
                    }
                }
            }
        }

        return map;
    }

    public boolean testRequirements(Player player, String id) {
        var quest = plugin.getConfigManager().getQuest(id);
        if (quest.getRequirements() == null) return true;
        for (Requirement req : quest.getRequirements()) {
            if (!req.test(player)) return false;
        }
        return true;
    }

    public QuestStatus getStatus(Player player, String id) {
        var quester = Questers.get(player.getName());
        if (quester.getCurrentQuests().containsKey(id)) return QuestStatus.IN_PROGRESS;
        if (!testRequirements(player, id)) return QuestStatus.DONT_MEET_REQUIREMENTS;

        // Havent not done yet
        if (!quester.getCompletedQuests().containsKey(id)) return QuestStatus.CAN_DO;

        // Meet requirements = true
        var categoryID = plugin.getCategoryManager().getCategory(id);
        if (categoryID == null) return QuestStatus.CAN_DO;

        // Have in category
        var category = plugin.getConfigManager().getCategory(categoryID);
        if (category.isAllowRedo() && category.getList().contains(id)) {
            if (category.getCooldown().enough(Utils.parse(quester.getCompletedQuests().get(id)), LocalDate.now())) return QuestStatus.CAN_DO;
        }

        return QuestStatus.CANT_REDO;
    }

    public void checkWrongQuests(Player player) {
        var quester = Questers.get(player.getName());
        boolean change = false;
        for (String id : quester.getCurrentQuests().keySet()) {
            if (plugin.getConfigManager().getQuest(id) == null) {
                quester.removeCurrent(id);
                change = true;
            }
        }
        if (change) Questers.save(player.getName());
    }

    public void checkExpiredQuests(Player player) {
        var quester = Questers.get(player.getName());
        boolean change = false;
        int count = 0;
        for (String id : quester.getCurrentQuests().keySet()) {
            var q = plugin.getConfigManager().getQuest(id);
            var data = quester.getCurrentQuests().get(id);
            var cs = plugin.getCategoryManager().getCategory(id);
            var category = plugin.getConfigManager().getCategory(cs);
            if (category == null || category.getCooldown() == null) continue;

            // Expired
            if (category.getCooldown().enough(LocalDate.now(), new Timestamp(data.getTakeTime()).toLocalDateTime().toLocalDate())) {
                quester.getCurrentQuests().remove(id);
                change = true;
                count++;
            }
        }
        if (count > 0) player.sendMessage("§c§oĐã hủy " + count + " nhiệm vụ vì quá hạn thời gian làm!");
        if (change) Questers.save(player.getName());
    }

    public void clearData(Player player) {
        var quester = Questers.get(player.getName());
        quester.setQuestPoints(0);
        quester.clearCompletedQuests();
        for (String qid : quester.getCurrentQuests().keySet()) {
            cancelQuest(player, qid);
        }
        quester.save();
    }

    public boolean checkMainQuest(Player p) {
        String mainQuest = null;
        var quester = Questers.get(p.getName());
        for (String id : quester.getCurrentQuests().keySet()) {
            if (Quests.get().getConfigManager().getFeatherboardQuests().contains(id)) mainQuest = id;
        }
        if (mainQuest == null) return false;

        var stage = Quests.get().getQuestManager().getCurrentStage(p, mainQuest);

        Quests.get().getBoardManager().sendBossbar(p);
        Quests.get().getBoardManager().openBoard(p, -1, false);

        return true;
    }

}
