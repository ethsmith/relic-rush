package dev.ethans.relicrush.messages;

import dev.ethans.relicrush.RelicRushPlugin;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class BossBarMessage {

    private static final RelicRushPlugin plugin = RelicRushPlugin.getInstance();

    public static void send(Player player, String message, double duration) {
        BossBar bossBar = plugin.getServer().createBossBar(message, BarColor.YELLOW, BarStyle.SOLID);
        bossBar.setProgress(1.0);

        bossBar.addPlayer(player);

        double progressToRemove = 1.0 / (duration * 20);
        new BukkitRunnable() {
            @Override
            public void run() {
                bossBar.setProgress(bossBar.getProgress() - progressToRemove);
                if (bossBar.getProgress() <= 0) {
                    bossBar.removePlayer(player);
                    bossBar.setVisible(false);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }

    public static void send(Player[] players, String message, double progress) {
        BossBar bossBar = plugin.getServer().createBossBar(message, BarColor.YELLOW, BarStyle.SOLID);
        bossBar.setProgress(progress);

        for (Player player : players)
            bossBar.addPlayer(player);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            bossBar.removeAll();
            bossBar.setVisible(false);
        }, 20);
    }

    public static void sendTimer(Player player, String message, int duration) {
        BossBar bossBar = plugin.getServer().createBossBar(message.replace("{time}", String.format("%02dm %02ds", duration / 60, duration % 60)), BarColor.YELLOW, BarStyle.SOLID);
        bossBar.setProgress(1.0);

        bossBar.addPlayer(player);

        AtomicInteger timePassed = new AtomicInteger(0);

        new BukkitRunnable() {
            @Override
            public void run() {
                int timeLeft = (duration - timePassed.incrementAndGet());

                double progressLeft = (double) (duration - timePassed.get()) / duration;
                bossBar.setProgress(progressLeft);

                String formattedTimeLeft = String.format("%02dm %02ds", timeLeft / 60, timeLeft % 60);

                bossBar.setTitle(message.replace("{time}", formattedTimeLeft));
                if (bossBar.getProgress() <= 0) {
                    bossBar.removePlayer(player);
                    bossBar.setVisible(false);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 20, 20);
    }
}
