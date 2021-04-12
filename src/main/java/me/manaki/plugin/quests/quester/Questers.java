package me.manaki.plugin.quests.quester;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mk.plugin.playerdata.storage.PlayerDataAPI;

import java.util.Map;

public class Questers {

    public static final String HOOK = "quests";
    private static final String KEY = "quester";

    private static final Map<String, Quester> questers = Maps.newHashMap();

    private static final Gson gson = new GsonBuilder().create();

    public static Quester get(String name) {
        if (!questers.containsKey(name)) questers.put(name, load(name));
        return questers.get(name);
    }

    public static Quester load(String name) {
        var data = PlayerDataAPI.get(name, HOOK);
        if (data.hasData(KEY)) {
            return gson.fromJson(data.getValue(KEY), Quester.class);
        }
        return new Quester(name);
    }

    public static void save(String name) {
        var quester = get(name);
        var data = PlayerDataAPI.get(name, HOOK);
        data.set(KEY, gson.toJson(quester));
        data.save();
    }

    public static void saveAndClearCache(String name) {
        var quester = get(name);
        var data = PlayerDataAPI.get(name, HOOK);
        data.set(KEY, gson.toJson(quester));
        data.save();
        questers.remove(name);
    }

}
