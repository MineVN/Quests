package me.manaki.plugin.quests.board;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import com.google.common.collect.Lists;
import me.manaki.plugin.quests.Quests;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

public class BoardManager {

    private final Quests plugin;

    private final List<String> toggledOn;

    public BoardManager(Quests plugin) {
        this.plugin = plugin;
        this.toggledOn = Lists.newArrayList();
    }

    public void openBoard(Player player, int seconds, boolean message) {
        var board = plugin.getConfigManager().getFeatherboard();
        if (board == null) return;
        toggledOn.add(player.getName());
        FeatherBoardAPI.showScoreboard(player, board);
        if (seconds != -1) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                closeBoard(player, false);
            }, seconds * 20L);
        }
        if (message) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
            player.sendMessage("");
            player.sendMessage("§aĐã chuyển sang chế độ Bảng Nhiệm vụ");
            player.sendMessage("§aGhi: §c/bangnhiemvu §ađể về bảng mặc định, ghi lần nữa để quay trở lại Bảng nhiệm vụ");
            player.sendMessage("");
        }
    }

    public void closeBoard(Player player, boolean message) {
        var board = plugin.getConfigManager().getFeatherboard();
        FeatherBoardAPI.removeScoreboardOverride(player, board);
        toggledOn.remove(player.getName());
        if (message) {
            player.sendMessage("§aHoàn thành nhiệm vụ chính, tự động đóng Bảng Nhiệm vụ");
        }
    }

    public boolean isToggledOn(Player player) {
        return toggledOn.contains(player.getName());
    }

}
