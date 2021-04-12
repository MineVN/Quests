package me.manaki.plugin.quests.quest;

import com.google.common.collect.Lists;
import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quester.Questers;
import mk.plugin.santory.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public enum QuestStatus {

    IN_PROGRESS {
        @Override
        public ItemStack getIcon(Player player, String id) {
            var quest = Quests.get().getConfigManager().getQuest(id);
            var is = new ItemStack(Material.PAPER);
            var meta = is.getItemMeta();
            meta.setCustomModelData(4);
            List<String> lore = Lists.newArrayList();

            var quester = Questers.get(player.getName());
            var data = quester.getCurrentQuests().get(id);

            var stage = Quests.get().getQuestManager().getCurrentStage(player, id);
            var objective = stage.getObjective();
            var tip = stage.getTip();

            meta.setDisplayName("§a§l" + quest.getName());
            lore.add("§aTiến trình: " + data.getStage() + "/" + quest.getStages().size());
            lore.add("");
            lore.addAll(Utils.toList(objective, 23, "§f§o"));
            if (tip != null) {
                lore.add("");
                lore.addAll(Utils.toList(tip, 23, "§7§o"));
            }
            if (quest.getRewardDesc() != null) {
                lore.add("");
                lore.add("§aPhần thưởng:");
                lore.addAll(quest.getRewardDesc());
            }
            lore.add("");
            lore.add("§cShift + Click để hủy n.vụ");

            meta.setLore(lore);
            is.setItemMeta(meta);

            return is;
        }
    },
    CANT_REDO {
        @Override
        public ItemStack getIcon(Player player, String id) {
            var quest = Quests.get().getConfigManager().getQuest(id);
            var is = new ItemStack(Material.PAPER);
            var meta = is.getItemMeta();
            meta.setCustomModelData(2);
            List<String> lore = Lists.newArrayList();

            meta.setDisplayName("§f§l" + quest.getName());
            lore.add("§fĐã hoàn thành, không thể làm lại");

            meta.setLore(lore);
            is.setItemMeta(meta);

            return is;
        }
    },
    CAN_DO {
        @Override
        public ItemStack getIcon(Player player, String id) {
            var quest = Quests.get().getConfigManager().getQuest(id);
            var is = new ItemStack(Material.PAPER);
            var meta = is.getItemMeta();
            meta.setCustomModelData(1);
            List<String> lore = Lists.newArrayList();

            var quester = Questers.get(player.getName());
            var data = quester.getCurrentQuests().get(id);

            meta.setDisplayName("§a§l" + quest.getName());
            lore.addAll(Utils.toList(quest.getDesc(), 23, "§f"));

            lore.add("");
            if (quest.getRewardDesc() != null) {
                lore.add("§aPhần thưởng:");
                lore.addAll(quest.getRewardDesc());
            }

            lore.add("");
            lore.add("§aClick để nhận");

            meta.setLore(lore);
            is.setItemMeta(meta);

            return is;
        }
    },
    DONT_MEET_REQUIREMENTS {
        @Override
        public ItemStack getIcon(Player player, String id) {
            var quest = Quests.get().getConfigManager().getQuest(id);
            var is = new ItemStack(Material.PAPER);
            var meta = is.getItemMeta();
            meta.setCustomModelData(3);
            List<String> lore = Lists.newArrayList();

            var quester = Questers.get(player.getName());

            meta.setDisplayName("§c§l" + quest.getName());
            lore.addAll(Utils.toList(quest.getDesc(), 23, "§f"));

            lore.add("");
            lore.addAll(Utils.toList(quest.getRequirementDesc(), 23, "§c§o"));

            meta.setLore(lore);
            is.setItemMeta(meta);

            return is;
        }
    };

    QuestStatus() {}

    public abstract ItemStack getIcon(Player player, String id);

}
