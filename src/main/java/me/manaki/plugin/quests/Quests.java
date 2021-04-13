package me.manaki.plugin.quests;

import me.manaki.plugin.quests.board.BoardManager;
import me.manaki.plugin.quests.command.AdminCommand;
import me.manaki.plugin.quests.command.PlayerCommand;
import me.manaki.plugin.quests.config.ConfigManager;
import me.manaki.plugin.quests.gui.GUIManager;
import me.manaki.plugin.quests.listener.GUIListener;
import me.manaki.plugin.quests.listener.PlayerListener;
import me.manaki.plugin.quests.listener.hook.CraftListener;
import me.manaki.plugin.quests.listener.hook.DungeonListener;
import me.manaki.plugin.quests.listener.hook.SantoryListener;
import me.manaki.plugin.quests.listener.hook.ShopListener;
import me.manaki.plugin.quests.placeholder.QuestPlaceholder;
import me.manaki.plugin.quests.quest.QuestManager;
import me.manaki.plugin.quests.category.CategoryManager;
import me.manaki.plugin.quests.quester.Questers;
import me.manaki.plugin.quests.task.QuestTask;
import me.manaki.plugin.quests.task.hook.PlaceholderTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Quests extends JavaPlugin {

    private ConfigManager configManager;
    private QuestManager questManager;
    private CategoryManager categoryManager;
    private GUIManager guiManager;
    private BoardManager boardManager;

    private PluginManager pluginManager;

    @Override
    public void onEnable() {
        // Config
        this.configManager = new ConfigManager(this);
        this.questManager = new QuestManager(this);
        this.categoryManager = new CategoryManager(this);
        this.guiManager = new GUIManager(this);
        this.boardManager = new BoardManager(this);
        this.pluginManager = Bukkit.getPluginManager();
        this.configManager.reload();

        // Command
        this.getCommand("quests").setExecutor(new AdminCommand(this));
        this.getCommand("questboard").setExecutor(new PlayerCommand(this));

        // Listener
        pluginManager.registerEvents(new PlayerListener(), this);
        pluginManager.registerEvents(new GUIListener(this), this);
        if (pluginManager.isPluginEnabled("Shops")) pluginManager.registerEvents(new ShopListener(this), this);
        if (pluginManager.isPluginEnabled("SantoryCore")) pluginManager.registerEvents(new SantoryListener(this), this);
        if (pluginManager.isPluginEnabled("Dungeons")) pluginManager.registerEvents(new DungeonListener(this), this);
        if (pluginManager.isPluginEnabled("Crafts")) pluginManager.registerEvents(new CraftListener(this), this);

        // Task
        new QuestTask(this).runTaskTimerAsynchronously(this, 0, 20);
        if (pluginManager.isPluginEnabled("PlaceholderAPI")) new PlaceholderTask(this).runTaskTimer(this, 0, 20);

        // Placeholder
        new QuestPlaceholder().register();
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Questers.saveAndClearCache(player.getName());
        }
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    public GUIManager getGUIManager() {
        return guiManager;
    }

    public BoardManager getBoardManager() {
        return boardManager;
    }

    public static Quests get() {
        return JavaPlugin.getPlugin(Quests.class);
    }

}
