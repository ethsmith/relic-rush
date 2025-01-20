package dev.ethans.relicrush.scoreboard;

import dev.ethans.relicrush.RelicRushPlugin;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;

public class WaitingScoreboard {

    private static final RelicRushPlugin plugin = RelicRushPlugin.getInstance();

    @Getter
    private static Sidebar sidebar;

    public static void createScoreboard() {
        if (sidebar  != null) return;

        sidebar = plugin.getScoreboardLibrary().createSidebar();

        sidebar.title(Component.text("Relic Rush", NamedTextColor.GOLD));
        sidebar.line(0, Component.empty());
        sidebar.line(1, Component.text("Waiting for players...", NamedTextColor.AQUA));
        sidebar.line(2, Component.empty());
        sidebar.line(3, Component.text("Players:", NamedTextColor.AQUA));

        TextComponent playersWaiting = Component.text()
                .content(String.valueOf(plugin.getServer().getOnlinePlayers().size()))
                .color(NamedTextColor.GREEN)
                .append(Component.text(" / ")
                        .color(NamedTextColor.AQUA))
                .append(Component.text(plugin.getMinigameConfig().getMinPlayers())
                        .color(NamedTextColor.GREEN))
                .build();

        sidebar.line(4, playersWaiting);
    }
}
