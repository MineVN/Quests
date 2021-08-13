package me.manaki.plugin.quests.category;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.manaki.plugin.dungeons.util.Utils;
import me.manaki.plugin.quests.Quests;
import mk.plugin.playerdata.storage.PlayerDataAPI;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class CategoryManager {

    private final Quests plugin;

    private LocalDate lastTime;
    private final Map<String, List<String>> availables;

    public CategoryManager(Quests plugin) {
        this.plugin = plugin;
        this.availables = Maps.newHashMap();
        this.lastTime = LocalDate.MIN;
    }

    public void check() {
        var globaldata = PlayerDataAPI.getGlobalData();

        // Get date
        LocalDate date = null;
        if (globaldata.hasData("quests-last-time")) {
            var v = globaldata.getValue("quests-last-time");
            int day = Integer.parseInt(v.split("/")[0]);
            int month = Integer.parseInt(v.split("/")[1]);
            int year = Integer.parseInt(v.split("/")[2]);
            date = LocalDate.of(year, month, day);
        }

        // Dont need to do anything
        if (lastTime.isEqual(LocalDate.now())) return;

        // Need get from data
        if (date != null && globaldata.hasData("quests-last-time") && !this.lastTime.equals(LocalDate.now()) && date.isEqual(LocalDate.now())) {
            for (String key : plugin.getConfigManager().getCategories().keySet()) {
                var datakey = "quests-avaiable-" + key;
                if (globaldata.hasData(datakey)) availables.put(key, Utils.from(globaldata.getValue(datakey), ";"));
            }
            this.lastTime = LocalDate.now();
            return;
        }

        // Generate availables
        generate(date);
    }

    public void generate(LocalDate date) {
        this.availables.clear();
        for (Map.Entry<String, Category> e : plugin.getConfigManager().getCategories().entrySet()) {
            var id = e.getKey();
            var category = e.getValue();
            if (date != null && category.isCooldownEnable() && !category.getCooldown().enough(date, LocalDate.now())) continue;
            this.availables.put(id, generateAvailables(category));
        }
        this.lastTime = LocalDate.now();
        save();
    }

    public void generate() {
        generate(null);
    }

    public void save() {
        var globaldata = PlayerDataAPI.getGlobalData();
        globaldata.set("quests-last-time", lastTime.getDayOfMonth() + "/" + lastTime.getMonthValue() + "/" + lastTime.getYear());
        for (Map.Entry<String, List<String>> e : this.availables.entrySet()) {
            globaldata.set("quests-avaiable-" + e.getKey(), Utils.to(e.getValue(), ";"));
        }
        PlayerDataAPI.saveGlobalData();
    }

    private List<String> generateAvailables(Category category) {
        List<String> result = Lists.newArrayList();

        boolean sort = false;
        for (Map.Entry<String, QuestGroup> e : category.getQuestGroups().entrySet()) {
            var gr = e.getValue();
            int ran = gr.getRandom();

            // random -1 == no random
            if (ran == -1) {
                result.addAll(gr.getQuests());
                continue;
            }

            // Randomize
            var clone = new ArrayList<> (gr.getQuests());
            if (clone.size() < ran) return clone;
            while (ran != 0) {
                ran--;
                int i = new Random().nextInt(clone.size());
                result.add(clone.get(i));
                clone.remove(i);
            }
            sort = true;
        }

        if (sort) Collections.sort(result);

        return result;
    }

    public String getCategory(String questID) {
        for (Map.Entry<String, Category> e : plugin.getConfigManager().getCategories().entrySet()) {
            if (e.getValue().containsQuest(questID)) return e.getKey();
        }
        return null;
    }

    public List<String> getAvailables(String categoryID) {
        return availables.getOrDefault(categoryID, Lists.newArrayList());
    }

    public Map<String, List<String>> getAvailables() {
        return availables;
    }

}
