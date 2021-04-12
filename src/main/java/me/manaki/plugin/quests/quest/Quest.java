package me.manaki.plugin.quests.quest;

import me.manaki.plugin.quests.quest.requirement.Requirement;
import me.manaki.plugin.quests.quest.stage.Stage;
import me.manaki.plugin.quests.utils.command.Command;

import java.util.List;

public class Quest {

    private final String id, name, desc, requirementDesc;
    private final List<String> rewardDesc;

    private final List<Requirement> requirements;
    private final List<Stage> stages;

    private final List<Command> rewardCommands;

    public Quest(String id, String name, String desc, String requirementDesc, List<String> rewardDesc, List<Requirement> requirements, List<Stage> stages, List<Command> rewardCommands) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.requirementDesc = requirementDesc;
        this.rewardDesc = rewardDesc;
        this.requirements = requirements;
        this.stages = stages;
        this.rewardCommands = rewardCommands;
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getRequirementDesc() {
        return requirementDesc;
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public List<String> getRewardDesc() {
        return rewardDesc;
    }

    public List<Command> getRewardCommands() {
        return rewardCommands;
    }
}
