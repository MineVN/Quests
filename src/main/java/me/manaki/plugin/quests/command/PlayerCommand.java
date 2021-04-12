package me.manaki.plugin.quests.command;

import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quester.Questers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlayerCommand implements CommandExecutor {

    private final Quests plugin;

    public PlayerCommand(Quests plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        var player = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("questboard")) {
            var quester = Questers.get(player.getName());
            String id = null;
            for (String s : quester.getCurrentQuests().keySet()) {
                if (plugin.getConfigManager().getFeatherboardQuests().contains(s)) id = s;
            }
            if (id == null) {
                player.sendMessage("§cBạn không có Nhiệm vụ chính nào!");
                return false;
            }
            if (plugin.getBoardManager().isToggledOn(player)) {
                plugin.getBoardManager().closeBoard(player, false);
            }
            else {
                plugin.getBoardManager().openBoard(player, -1, false);
                player.sendMessage("§aĐể chuyển về mặc định hoặc bật, ghi: §c/bangnhiemvu");
            }
        }

        return false;
    }

}
