package me.manaki.plugin.quests.category;

import java.util.Map;

public class Category {

    private final String name;
    private final boolean cooldownEnable;
    private final CooldownType cooldown;

    private final Map<String, QuestGroup> questGroups;

    public Category(String name, boolean cooldownEnable, CooldownType cooldown, Map<String, QuestGroup> questGroups) {
        this.name = name;
        this.cooldownEnable = cooldownEnable;
        this.cooldown = cooldown;
        this.questGroups = questGroups;
    }

    public String getName() {
        return name;
    }

    public boolean isAllowRedo() {
        return cooldownEnable;
    }

    public boolean isCooldownEnable() {
        return cooldownEnable;
    }

    public CooldownType getCooldown() {
        return cooldown;
    }

    public Map<String, QuestGroup> getQuestGroups() {
        return questGroups;
    }

    public boolean containsQuest(String id) {
        for (QuestGroup gr : this.questGroups.values()) {
            if (gr.getQuests().contains(id)) return true;
        }
        return false;
    }

}
