package me.manaki.plugin.quests.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.manaki.plugin.quests.quest.Quest;
import me.manaki.plugin.quests.quest.requirement.Requirement;
import me.manaki.plugin.quests.quest.requirement.RequirementType;
import me.manaki.plugin.quests.quest.stage.Stage;
import me.manaki.plugin.quests.quest.stage.StageType;
import me.manaki.plugin.quests.category.Category;
import me.manaki.plugin.quests.category.CooldownType;
import me.manaki.plugin.quests.utils.command.Command;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigManager {

    private final Plugin plugin;

    private String featherboard;
    private List<String> featherboardAutoEnables;

    private FileConfiguration pluginConfig;

    private final Map<String, Quest> quests = Maps.newHashMap();
    private final Map<String, Category> categories = Maps.newHashMap();

    private List<Command> onQuestStartCommands = Lists.newArrayList();
    private List<Command> onQuestFinishCommands = Lists.newArrayList();
    private List<Command> onQuestCancelCommands = Lists.newArrayList();
    private List<Command> onStageChangeCommands = Lists.newArrayList();

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void reload() {
        // Plugin config
        plugin.saveDefaultConfig();
        pluginConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        checkDefaultConfig();
        loadReadme();

        // Check folder
        var folder = new File(plugin.getDataFolder() + "//quests");
        if (!folder.exists()) {
            folder.mkdirs();
            var exampleQuest = plugin.getResource("example-quest.yml");
            var file = new File(folder.getPath() + "//example-quest.yml");
            try {
                FileUtils.copyInputStreamToFile(exampleQuest, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Quests
        quests.clear();
        for (File file : folder.listFiles()) {
            var id = file.getName().replace(".yml", "");
            quests.put(id, loadQuest(id));
        }

        // config.yml
        onQuestStartCommands = pluginConfig.getStringList("on-quest-start-commands").stream().map(Command::new).collect(Collectors.toList());
        onQuestFinishCommands = pluginConfig.getStringList("on-quest-finish-commands").stream().map(Command::new).collect(Collectors.toList());
        onQuestCancelCommands = pluginConfig.getStringList("on-quest-cancel-commands").stream().map(Command::new).collect(Collectors.toList());
        onStageChangeCommands = pluginConfig.getStringList("on-stage-change-commands").stream().map(Command::new).collect(Collectors.toList());

        // Categories
        categories.clear();
        for (String id : pluginConfig.getConfigurationSection("category").getKeys(false)) {
            categories.put(id, loadCategory(pluginConfig.getConfigurationSection("category." + id)));
        }

        // Featherboard
        if (pluginConfig.getBoolean("featherboard.enable")) {
            featherboard = pluginConfig.getString("featherboard.board");
            featherboardAutoEnables = pluginConfig.getStringList("featherboard.quests");
        }

    }

    private void loadReadme() {
        var is = plugin.getResource("readme.txt");
        var file = new File(plugin.getDataFolder(), "readme.txt");
        try {
            FileUtils.copyInputStreamToFile(is, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Quest getQuest(String id) {
        return quests.getOrDefault(id, null);
    }

    public Category getCategory(String id) {
        return categories.getOrDefault(id, null);
    }

    public Map<String, Category> getCategories() {
        return categories;
    }

    public List<Command> getOnQuestStartCommands() {
        return onQuestStartCommands;
    }

    public List<Command> getOnQuestFinishCommands() {
        return onQuestFinishCommands;
    }

    public List<Command> getOnQuestCancelCommands() {
        return onQuestCancelCommands;
    }

    public String getFeatherboard() {
        return featherboard;
    }

    public List<String> getFeatherboardQuests() {
        return featherboardAutoEnables;
    }

    private void checkDefaultConfig() {
        File file = null;
        try {
            file = File.createTempFile("config", "yml");
            var is = plugin.getResource("config.yml");
            assert is != null;
            FileUtils.copyInputStreamToFile(is, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var defaultconfig = YamlConfiguration.loadConfiguration(file);

        boolean change = false;
        for (String key : defaultconfig.getConfigurationSection("").getKeys(false)) {
            if (!pluginConfig.contains(key)) {
                pluginConfig.set(key, defaultconfig.get(key));
                change = true;
            }
        }
        if (change) {
            try {
                pluginConfig.save(new File(plugin.getDataFolder(), "config.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Quest loadQuest(String id) {
        var folder = new File(plugin.getDataFolder() + "//quests");
        var config = YamlConfiguration.loadConfiguration(new File(folder.getPath() + "//" + id + ".yml"));
        var getter = ConfigGetter.from(config);

        var name = getter.getString("name", "Insert new name please");
        var desc = getter.getString("desc");
        var requirementDesc = getter.getString("requirement-desc", null);
        var rewardDesc = getter.getStringList("reward-desc");
        var rewardCommands = getter.getStringList("reward-commands").stream().map(Command::new).collect(Collectors.toList());
        List<Requirement> requirements = null;
        if (config.contains("requirements") && config.get("requirements") != null) {
            requirements = Lists.newArrayList();
            int i = 0;
            while (config.contains("requirements." + i)) {
                var type = RequirementType.valueOf(getter.getString("requirements." + i + ".type"));
                var value = getter.getString("requirements." + i + ".value");
                requirements.add(new Requirement(type, value));
                i++;
            }
        }
        List<Stage> stages = Lists.newArrayList();
        int i = 0;
        while (config.contains("stages." + i)) {
            var path = "stages." + i;
            var type = StageType.valueOf(getter.getString(path + ".type"));
            var objective = getter.getString(path + ".objective");
            var tip = getter.getString(path + ".tip");
            var count = getter.getInt(path + ".count", 1);
            Map<String, String> data = Maps.newHashMap();
            for (String key : config.getConfigurationSection(path + ".data").getKeys(false)) {
                var value = getter.getString(path + ".data." + key);
                data.put(key, value);
            }
            var onCountAddedCommands = getter.getStringList(path + ".on-count-added-commands", Lists.newArrayList()).stream().map(Command::new).collect(Collectors.toList());
            var onStartCommands = getter.getStringList(path + ".on-start-commands", Lists.newArrayList()).stream().map(Command::new).collect(Collectors.toList());
            var onEndCommands = getter.getStringList(path + ".on-end-commands", Lists.newArrayList()).stream().map(Command::new).collect(Collectors.toList());
            stages.add(new Stage(type, objective, tip, count, data, onCountAddedCommands, onStartCommands, onEndCommands));
            i++;
        }

        return new Quest(id, name, desc, requirementDesc, rewardDesc, requirements, stages, rewardCommands);
    }

    private Category loadCategory(ConfigurationSection section) {
        var name = section.getString("name");
        boolean cooldownEnable = section.getBoolean("cooldown.enable");
        var cooldownType = section.contains("cooldown.type") ? CooldownType.valueOf(section.getString("cooldown.type")) : null;
        boolean randomEnable = section.getBoolean("random.enable");
        var random = section.getInt("random.amount");
        var list = section.getStringList("list");

        return new Category(name, cooldownEnable, cooldownType, randomEnable, random, list);
    }

    public List<Command> getOnStageChangeCommands() {
        return onStageChangeCommands;
    }

}
