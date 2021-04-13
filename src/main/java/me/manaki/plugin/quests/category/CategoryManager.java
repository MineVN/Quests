package me.manaki.plugin.quests.category;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.manaki.plugin.dungeons.util.Utils;
import me.manaki.plugin.quests.Quests;
import mk.plugin.playerdata.storage.PlayerDataAPI;

import java.time.LocalDate;
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
        else date = LocalDate.now();

        // Dont need to do anything
        if (lastTime != null && lastTime.isEqual(date)) return;

        // Need get from data
        if (globaldata.hasData("quests-last-time") && lastTime == null && LocalDate.now().isEqual(date)) {
            for (String key : plugin.getConfigManager().getCategories().keySet()) {
                var datakey = "quests-avaiable-" + key;
                if (globaldata.hasData(datakey)) availables.put(key, Utils.from(globaldata.getValue(datakey), ";"));
            }
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
        if (!category.isRandomEnable()) return category.getList();
        var ran = category.getRandom();

        List<String> result = Lists.newArrayList();
        var clone = new ArrayList<> (category.getList());
        if (clone.size() < ran) return clone;

        while (ran != 0) {
            ran--;
            int i = new Random().nextInt(clone.size());
            result.add(clone.get(i));
            clone.remove(i);
        }

        Collections.sort(result);

        return result;
    }

    public String getCategory(String questID) {
        for (Map.Entry<String, Category> e : plugin.getConfigManager().getCategories().entrySet()) {
            if (e.getValue().getList().contains(questID)) return e.getKey();
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
