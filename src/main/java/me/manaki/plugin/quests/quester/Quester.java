package me.manaki.plugin.quests.quester;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import me.manaki.plugin.quests.utils.Utils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Quester {

    private final String name;
    private int questPoints;
    private Map<String, String> completedQuests;
    private ConcurrentHashMap<String, QuestData> currentQuests;

    public Quester(String name) {
        this.name = name;
        this.questPoints = 0;
        this.completedQuests = Maps.newHashMap();
        this.currentQuests = new ConcurrentHashMap<>();
    }

    public Quester(String name, int questPoints, Map<String, String> completedQuests, ConcurrentHashMap<String, QuestData> currentQuests) {
        this.name = name;
        this.questPoints = questPoints;
        this.completedQuests = completedQuests;
        this.currentQuests = currentQuests;
    }

    public void save() {
        Questers.save(this.name);
    }

    public String getName() {
        return name;
    }

    public int getQuestPoints() {
        return questPoints;
    }

    public Map<String, String> getCompletedQuests() {
        return completedQuests;
    }

    public ConcurrentHashMap<String, QuestData> getCurrentQuests() {
        return currentQuests;
    }

    public void setQuestPoints(int questPoints) {
        this.questPoints = questPoints;
    }

    public void addCompletedQuest(String id) {
        this.completedQuests.put(id, Utils.toString(LocalDate.now()));
    }

    public void setCurrentQuests(ConcurrentHashMap<String, QuestData> currentQuests) {
        this.currentQuests = currentQuests;
    }

    public boolean addCurrentQuest(String questID, boolean override) {
        if (this.currentQuests.containsKey(questID) && !override) return false;
        this.currentQuests.put(questID, new QuestData(questID));
        return true;
    }

    public void setCurrenQuest(String id, QuestData data) {
        currentQuests.put(id, data);
    }
}
