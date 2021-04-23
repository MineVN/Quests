package me.manaki.plugin.quests.command;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quester.Questers;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.List;

public class AdminCommand implements CommandExecutor {

    private final Quests plugin;

    public AdminCommand(Quests plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        try {
            if (args[0].equalsIgnoreCase("reload")) {
                plugin.getConfigManager().reload();
                sender.sendMessage("§aAll reloaded!");
            }

            else if (args[0].equalsIgnoreCase("debugquest")) {
                var id = args[1];
                var quest = plugin.getConfigManager().getQuest(id);
                var gson = new GsonBuilder().create();
                sender.sendMessage(gson.toJson(quest));
            }

            else if (args[0].equalsIgnoreCase("debugcategory")) {
                var id = args[1];
                var category = plugin.getConfigManager().getCategory(id);
                var gson = new GsonBuilder().create();
                sender.sendMessage(gson.toJson(category));
            }

            else if (args[0].equalsIgnoreCase("give")) {
                var id = args[1];
                var player = Bukkit.getPlayer(args[2]);
                var override = Boolean.parseBoolean(args[3]);
                assert player != null : "Can't find player" + args[2];
                plugin.getQuestManager().give(player, id, override);
            }

            else if (args[0].equalsIgnoreCase("cancel")) {
                var id = args[1];
                var player = Bukkit.getPlayer(args[2]);
                assert player != null : "Can't find player" + args[2];
                plugin.getQuestManager().cancelQuest(player, id);
            }

            else if (args[0].equalsIgnoreCase("finish")) {
                var id = args[1];
                var player = Bukkit.getPlayer(args[2]);
                assert player != null : "Can't find player" + args[2];
                plugin.getQuestManager().finishQuest(player, id);
            }

            else if (args[0].equalsIgnoreCase("category")) {
                if (args[1].equalsIgnoreCase("reset")) {
                    plugin.getCategoryManager().generate();
                    sender.sendMessage("§aAll reset!");
                }
            }

            else if (args[0].equalsIgnoreCase("gui")) {
                if (args[1].equalsIgnoreCase("current")) {
                    var player = Bukkit.getPlayer(args[2]);
                    var quester = Questers.get(player.getName());
                    List<String> quests = Lists.newArrayList(quester.getCurrentQuests().keySet());
                    plugin.getGUIManager().open(player, quests, "§0§lNHIỆM VỤ ĐANG LÀM", 1, true);
                }
                else if (args[1].equalsIgnoreCase("category")) {
                    var id = args[2];
                    var player = Bukkit.getPlayer(args[3]);
                    var category = plugin.getConfigManager().getCategory(id);
                    plugin.getGUIManager().open(player, plugin.getCategoryManager().getAvailables(id), "§0§l" + category.getName().toUpperCase(), 1);
                }
            }

            else if (args[0].equalsIgnoreCase("cleardata")) {
                var player = Bukkit.getPlayer(args[1]);
                assert player != null;
                plugin.getQuestManager().clearData(player);
                sender.sendMessage("§aCleared!");
            }

        }
        catch (ArrayIndexOutOfBoundsException e) {
            sendHelp(sender);
        }

        return false;
    }

    public void sendHelp(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("§6§lQuests by MankaiStep - Minevn.net");
        sender.sendMessage("§e/quests reload");
        sender.sendMessage("§e/quests debugquest <*id>");
        sender.sendMessage("§e/quests debugcategory <*id>");
        sender.sendMessage("§e/quests give <*id> <*player> <*override>");
        sender.sendMessage("§e/quests cancel <*id> <*player>");
        sender.sendMessage("§e/quests finish <*id> <*player>");
        sender.sendMessage("§e/quests cleardata <*player>");
        sender.sendMessage("§e/quests category reset");
        sender.sendMessage("§e/quests gui current <*player>");
        sender.sendMessage("§e/quests gui category <*id> <*player>");
        sender.sendMessage("");
    }

}
