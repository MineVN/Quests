package me.manaki.plugin.quests.quest.requirement;

import me.manaki.plugin.quests.quester.Questers;
import org.bukkit.entity.Player;

import java.util.concurrent.locks.ReentrantLock;

public class Requirement {

    private final RequirementType type;
    private final String value;

    public Requirement(RequirementType type, String value) {
        this.type = type;
        this.value = value;
    }

    public RequirementType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean test(Player player) {
        return type.check(player, this.value);
    }

}
