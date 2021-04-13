package me.manaki.plugin.quests.category;


import java.util.List;

public class Category {

    private String name;
    private boolean cooldownEnable;
    private CooldownType cooldown;

    private boolean randomEnable;
    private int random;

    private List<String> list;

    public Category(String name, boolean cooldownEnable, CooldownType cooldown, boolean randomEnable, int random, List<String> list) {
        this.name = name;
        this.cooldownEnable = cooldownEnable;
        this.cooldown = cooldown;
        this.randomEnable = randomEnable;
        this.random = random;
        this.list = list;
    }

    public Category(String name, boolean allowRedo, List<String> list) {
        this(name, false, null, false, -1, list);
    }

    public Category(String name, boolean allowRedo, CooldownType cooldown, List<String> list) {
        this(name, true, cooldown, false, -1, list);
    }

    public Category(String name, boolean allowRedo, int random, List<String> list) {
        this(name, false, null, true, random, list);
    }

    public Category(String name, boolean allowRedo, CooldownType cooldown, int random, List<String> list) {
        this(name, true, cooldown, true, random, list);
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

    public boolean isRandomEnable() {
        return randomEnable;
    }

    public int getRandom() {
        return random;
    }

    public List<String> getList() {
        return list;
    }
}
