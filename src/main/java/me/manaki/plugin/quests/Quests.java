package me.manaki.plugin.quests;

import me.manaki.plugin.quests.board.BoardManager;
import me.manaki.plugin.quests.command.AdminCommand;
import me.manaki.plugin.quests.command.PlayerCommand;
import me.manaki.plugin.quests.config.ConfigManager;
import me.manaki.plugin.quests.gui.GUIManager;
import me.manaki.plugin.quests.listener.GUIListener;
import me.manaki.plugin.quests.listener.PlayerListener;
import me.manaki.plugin.quests.listener.hook.*;
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

    private boolean isLoaded;

    @Override
    public void onEnable() {
        // Config
        this.configManager = new ConfigManager(this);
        this.questManager = new QuestManager(this);
        this.categoryManager = new CategoryManager(this);
        this.guiManager = new GUIManager(this);
        this.boardManager = new BoardManager(this);
        this.configManager.reload();

        // Command
        this.getCommand("quests").setExecutor(new AdminCommand(this));
        this.getCommand("questboard").setExecutor(new PlayerCommand(this));

        // Listener
        var pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new GUIListener(this), this);
        pm.registerEvents(new ConversationListener(this), this);

        // Check if has player >> load
        if (Bukkit.getOnlinePlayers().size() > 0) {
            this.isLoaded = true;
            load();
        }

        // Placeholder
        new QuestPlaceholder().register();

        // Check wrong quests
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.getQuestManager().checkWrongQuests(player);
        }
    }

    public void load() {
        var pm = Bukkit.getPluginManager();
        pm.registerEvents(new ShopListener(this), this);
        pm.registerEvents(new SantoryListener(this), this);
        pm.registerEvents(new DungeonListener(this), this);
        pm.registerEvents(new CraftListener(this), this);
        pm.registerEvents(new MythicMobListener(this), this);
        pm.registerEvents(new MarketListener(this), this);
        pm.registerEvents(new SkybattleListener(this), this);

        // Task
        new QuestTask(this).runTaskTimerAsynchronously(this, 0, 20);
        if (pm.isPluginEnabled("PlaceholderAPI")) new PlaceholderTask(this).runTaskTimer(this, 0, 20);
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

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean value) {
        this.isLoaded = value;
    }
}
