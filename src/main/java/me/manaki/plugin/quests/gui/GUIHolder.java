package me.manaki.plugin.quests.gui;

import com.google.common.collect.Maps;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class GUIHolder implements InventoryHolder {

    private final List<String> quests;
    private int currentPage;
    private String title;
    private boolean isCurrent;

    private Map<Integer, String> slots;

    public GUIHolder(List<String> quests, String title, int page, boolean isCurrent) {
        this.quests = quests;
        this.currentPage = page;
        this.title = title;
        this.isCurrent = isCurrent;
        this.slots = Maps.newHashMap();
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getQuests() {
        return quests;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void set(int slot, String quest) {
        slots.put(slot, quest);
    }

    public String getQuestInSlot(int slot) {
        return slots.getOrDefault(slot, null);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
