package me.manaki.plugin.quests.quest.stage;

import me.manaki.plugin.quests.utils.command.Command;
import me.manaki.plugin.quests.utils.data.DataValue;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class Stage {

    private final StageType type;
    private final String objective;
    private final String tip;
    private final int count;
    private final Map<String, String> data;

    private final List<Command> onCountAddedCommands;
    private final List<Command> onStartCommands;
    private final List<Command> onEndCommands;

    public Stage(StageType type, String objective, String tip, int count, Map<String, String> data, List<Command> onCountAddedCommands, List<Command> onStartCommands, List<Command> onEndCommands) {
        this.type = type;
        this.objective = objective;
        this.tip = tip;
        this.count = count;
        this.data = data;
        this.onCountAddedCommands = onCountAddedCommands;
        this.onStartCommands = onStartCommands;
        this.onEndCommands = onEndCommands;
    }

    public StageType getType() {
        return type;
    }

    public String getObjective() {
        return objective;
    }

    public String getTip() {
        return tip;
    }

    public int getCount() {
        return count;
    }

    public Map<String, String> getData() {
        return data;
    }

    public List<Command> getOnCountAddedCommands() {
        return onCountAddedCommands;
    }

    public List<Command> getOnStartCommands() {
        return onStartCommands;
    }

    public List<Command> getOnEndCommands() {
        return onEndCommands;
    }

    public boolean match(Map<String, Object> values) {
        var model = this.type.getDefaultDataModel();
        for (Map.Entry<String, Object> e : values.entrySet()) {
            var key = e.getKey();
            if (!this.data.containsKey(key)) continue;
            var dv = new DataValue(model.get(key), data.get(key));
            if (!dv.compare(e.getValue())) return false;
        }
        return true;
    }

    public void onStart(Player player, Map<String, String> placeholders) {
        for (Command cmd : this.getOnStartCommands()) cmd.execute(player, placeholders);
        this.type.onStart(player, this.count, this.data);
    }

    public void onEnd(Player player, Map<String, String> placeholders) {
        for (Command cmd : this.getOnEndCommands()) cmd.execute(player, placeholders);
        this.type.onEnd(player, this.count, this.data);
    }

}
