package me.manaki.plugin.quests.category;

import java.util.List;

public class QuestGroup {

    private final int random;
    private final List<String> quests;

    public QuestGroup(int random, List<String> quests) {
        this.random = random;
        this.quests = quests;
    }

    public int getRandom() {
        return random;
    }

    public List<String> getQuests() {
        return quests;
    }
}
