package me.manaki.plugin.quests.board;

import be.maximvdw.featherboard.api.FeatherBoardAPI;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.manaki.plugin.quests.Quests;
import me.manaki.plugin.quests.quester.Questers;
import me.manaki.plugin.quests.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class BoardManager {

    private final Quests plugin;

    private final List<String> toggledOn;
    private final Map<String, BossBar> bossbars;

    public BoardManager(Quests plugin) {
        this.plugin = plugin;
        this.toggledOn = Lists.newArrayList();
        this.bossbars = Maps.newConcurrentMap();
    }

    public void sendBossbar(Player player) {
        // Bossbar
        String mainQuest = null;
        var quester = Questers.get(player.getName());
        for (String id : quester.getCurrentQuests().keySet()) {
            if (Quests.get().getConfigManager().getFeatherboardQuests().contains(id)) mainQuest = id;
        }
        if (mainQuest != null) {
            var stage = Quests.get().getQuestManager().getCurrentStage(player, mainQuest);
            var tip = stage.getTip();
            if (tip != null) {
                // Remove current bossbar
                if (bossbars.containsKey(player.getName())) {
                    var bb = bossbars.get(player.getName());
                    bb.removePlayer(player);
                }

                // New bossbar
                var bb = Bukkit.createBossBar("§f" + Utils.recolorBold(tip), BarColor.GREEN, BarStyle.SOLID);
                bb.addPlayer(player);
                bossbars.put(player.getName(), bb);
            }
        }
    }

    public void removeBossbar(Player player) {
        // Remove current bossbar
        if (bossbars.containsKey(player.getName())) {
            var bb = bossbars.get(player.getName());
            bb.removeAll();
            bossbars.remove(player.getName());
        }
    }

    public void openBoard(Player player, int seconds, boolean message) {
        // Featherboard
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
            player.sendMessage("§aGhi: §c§l/bangnhiemvu §ađể về bảng mặc định, ghi lần nữa để quay trở lại Bảng nhiệm vụ");
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
