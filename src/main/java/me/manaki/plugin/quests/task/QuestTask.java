package me.manaki.plugin.quests.task;

import me.manaki.plugin.quests.Quests;
import org.bukkit.scheduler.BukkitRunnable;

public class QuestTask extends BukkitRunnable {

    private final Quests plugin;

    public QuestTask(Quests plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        plugin.getCategoryManager().check();
    }

}
