package me.manaki.plugin.quests.quest.stage;

import com.google.common.collect.Maps;
import me.clip.placeholderapi.PlaceholderAPI;
import me.manaki.plugin.quests.quester.Questers;
import me.manaki.plugin.quests.task.hook.PlaceholderTask;
import me.manaki.plugin.quests.utils.data.ValueType;
import mk.plugin.playerdata.storage.PlayerData;
import mk.plugin.playerdata.storage.PlayerDataAPI;
import org.bukkit.entity.Player;

import java.util.Map;

public enum StageType {

    PLACEHOLDER_CHANGE {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("placeholder", ValueType.STRING);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {
            var placeholder = data.get("placeholder");
            double value = Double.parseDouble(PlaceholderAPI.setPlaceholders(player, placeholder));
            double targetV = value + count;
            PlayerData pd = PlayerDataAPI.get(player, Questers.HOOK);
            pd.set(PlaceholderTask.KEY.replace("%placeholder%", placeholder), targetV + "");
            pd.save();
        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {
            var placeholder = data.get("placeholder");
            PlayerData pd = PlayerDataAPI.get(player, Questers.HOOK);
            pd.remove(PlaceholderTask.KEY.replace("%placeholder%", placeholder));
            pd.save();
        }
    },

    DUNGEON_FINISH {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("dungeon-id", ValueType.STRING);
            m.put("result", ValueType.STRING);
            m.put("mem-start", ValueType.NUMBER);
            m.put("mem-finish", ValueType.NUMBER);
            m.put("mob-kills", ValueType.NUMBER);
            m.put("slave-saves", ValueType.NUMBER);
            m.put("death-times", ValueType.NUMBER);
            m.put("play-time", ValueType.NUMBER);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    DUNGEON_MOB_KILL{
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("dungeon-id", ValueType.STRING);
            m.put("mob-id", ValueType.STRING);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    SORA_ITEM_ASCENT {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("ascent-level", ValueType.NUMBER);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    SORA_ITEM_ENHANCE {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("enhance-level", ValueType.NUMBER);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    SORA_ITEM_UPGRADE {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("upgrade-level", ValueType.NUMBER);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    SORA_ITEM_CRAFT {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("recipe-id", ValueType.STRING);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    SHOPS_ITEM_DELIVER {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("item-id", ValueType.STRING);
            m.put("npc-id", ValueType.NUMBER);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },
    CONVERSATION {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("npc-id", ValueType.NUMBER);
            for (int i = 0 ; i < 50 ; i++) {
                m.put("c-" + i, ValueType.STRING);
            }

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    SORA_WISH {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("wish", ValueType.STRING);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    LOCATION_REACH {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("warp", ValueType.STRING);
            m.put("radius", ValueType.NUMBER);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    COMMAND_EXECUTE {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("command", ValueType.STRING);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    BLOCK_BREAK {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("block-type", ValueType.STRING);
            m.put("world", ValueType.STRING);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    MYTHICMOB_KILL {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("mythicmob-id", ValueType.STRING);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    MARKET_SELL {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("item-material", ValueType.STRING);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    SKYBATTLE {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("top", ValueType.NUMBER);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    SHOPS_ITEM_BUY {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("item-id", ValueType.STRING);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    FISHING {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("item-type", ValueType.STRING);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    PLAYER_KILL {
        @Override
        public Map<String, ValueType> getDefaultDataModel() {
            Map<String, ValueType> m = Maps.newLinkedHashMap();
            m.put("player-name", ValueType.STRING);

            return m;
        }

        @Override
        public void onStart(Player player, int count, Map<String, String> data) {

        }

        @Override
        public void onEnd(Player player, int count, Map<String, String> data) {

        }
    },

    ;

    public abstract Map<String, ValueType> getDefaultDataModel();
    public abstract void onStart(Player player, int count, Map<String, String> data);
    public abstract void onEnd(Player player, int count, Map<String, String> data);

}
