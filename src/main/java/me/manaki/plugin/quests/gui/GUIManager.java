package me.manaki.plugin.quests.gui;

import com.google.common.collect.Lists;
import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quest.QuestStatus;
import me.manaki.plugin.quests.quester.Questers;
import mk.plugin.santory.utils.Tasks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GUIManager {

    private final List<Integer> QUEST_SLOTS = List.of(0, 1, 2, 3, 4, 5, 6, 9, 10, 11, 12, 13, 14, 15, 18, 19, 20, 21, 22, 23, 24);
    private final List<Integer> VERTICAL_BAR_SLOTS = List.of(7, 16, 25);
    private final int NEXT_SLOT = 8;
    private final int PREVIOUS_SLOT = 17;
    private final int INFO_SLOT = 26;

    private final Quests plugin;

    public GUIManager(Quests plugin) {
        this.plugin = plugin;
    }

    public void open(Player player, List<String> quests, String title, int page) {
        var holder = new GUIHolder(quests, title, page);
        int maxPage = getMaxPage(quests.size());
        var inv = Bukkit.createInventory(holder, 27, title + " [" + page + "/" + maxPage + "]");
        player.openInventory(inv);

        Tasks.async(() -> {
            for (int i : VERTICAL_BAR_SLOTS) inv.setItem(i, getVerticalBar());
            inv.setItem(NEXT_SLOT, getNextButton());
            inv.setItem(PREVIOUS_SLOT, getPreviousButton());
            inv.setItem(INFO_SLOT, getInfo(player));

            int index = QUEST_SLOTS.size() * (page - 1);
            int to = Math.min(quests.size(), index + QUEST_SLOTS.size());
            for (int i = index ; i < to ; i++) {
                var questID = quests.get(i);
                inv.setItem(QUEST_SLOTS.get(i), getQuestIcon(player, questID));
                holder.set(QUEST_SLOTS.get(i), questID);
            }
            for (int i = to ; i < QUEST_SLOTS.size() ; i++) {
                inv.setItem(QUEST_SLOTS.get(i), getBarrier());
            }
        });
    }

    public void onClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof GUIHolder)) return;
        e.setCancelled(true);

        var holder = (GUIHolder) e.getInventory().getHolder();
        var player = (Player) e.getWhoClicked();
        int slot = e.getSlot();

        if (slot == NEXT_SLOT) {
            if (holder.getCurrentPage() >= getMaxPage(holder.getQuests().size())) {
                player.sendMessage("§cĐây là trang cuối rồi!");
            }
            else open(player, holder.getQuests(), holder.getTitle(), holder.getCurrentPage() + 1);
        }
        else if (slot == PREVIOUS_SLOT) {
            if (holder.getCurrentPage() <= 1) {
                player.sendMessage("§cĐây là trang đầu!");
            }
            else open(player, holder.getQuests(), holder.getTitle(), holder.getCurrentPage() - 1);
        }
        else if (QUEST_SLOTS.contains(slot)) {
            var questID = holder.getQuestInSlot(slot);
            if (questID == null) return;
            var status = plugin.getQuestManager().getStatus(player, questID);
            if (status == QuestStatus.CAN_DO) {
                plugin.getQuestManager().give(player, questID, false);
                open(player, holder.getQuests(), holder.getTitle(), holder.getCurrentPage());
            }
            else if (status == QuestStatus.IN_PROGRESS && e.getClick() == ClickType.SHIFT_LEFT) {
                plugin.getQuestManager().cancelQuest(player, questID);
                open(player, holder.getQuests(), holder.getTitle(), holder.getCurrentPage());
            }
        }
    }

    public ItemStack getVerticalBar() {
        var is = new ItemStack(Material.GLASS_PANE);
        var meta = is.getItemMeta();
        meta.setCustomModelData(21);
        meta.setDisplayName("§a");
        is.setItemMeta(meta);
        return is;
    }

    public ItemStack getNextButton() {
        var is = new ItemStack(Material.GLASS_PANE);
        var meta = is.getItemMeta();
        meta.setCustomModelData(22);
        meta.setDisplayName("§a§lTrang sau >>");
        is.setItemMeta(meta);
        return is;
    }

    public ItemStack getPreviousButton() {
        var is = new ItemStack(Material.GLASS_PANE);
        var meta = is.getItemMeta();
        meta.setCustomModelData(23);
        meta.setDisplayName("§a§l<< Trang trước");
        is.setItemMeta(meta);
        return is;
    }

    public ItemStack getInfo(Player player) {
        var is = new ItemStack(Material.GLASS_PANE);
        var meta = is.getItemMeta();
        meta.setCustomModelData(8);
        meta.setDisplayName("§a");

        var quester = Questers.get(player.getName());
        List<String> lore = Lists.newArrayList();
        meta.setDisplayName("§a§lThông tin");
        lore.add("§aNgười chơi: §f" + player.getName());
        lore.add("§aĐiểm n.vụ: §f" + quester.getQuestPoints());
        lore.add("§aN.vụ đang làm: §f" + quester.getCurrentQuests().size());
        lore.add("§aN.vụ đã xong: §f" + quester.getCompletedQuests().size());

        meta.setLore(lore);
        is.setItemMeta(meta);

        return is;
    }

    public ItemStack getBarrier() {
        var is = new ItemStack(Material.BARRIER);
        var meta = is.getItemMeta();
        meta.setDisplayName("§a");
        is.setItemMeta(meta);
        return is;
    }

    public ItemStack getQuestIcon(Player player, String id) {
        return plugin.getQuestManager().getStatus(player, id).getIcon(player, id);
    }

    public int getMaxPage(int size) {
        return Math.max(1, size % QUEST_SLOTS.size() == 0 ? size / QUEST_SLOTS.size() : size / QUEST_SLOTS.size() + 1);
    }

}
