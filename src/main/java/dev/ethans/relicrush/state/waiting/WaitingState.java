package dev.ethans.relicrush.state.waiting;

import dev.ethans.relicrush.RelicRushPlugin;
import dev.ethans.relicrush.state.base.GameState;
import dev.ethans.relicrush.state.shared.BlockListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class WaitingState extends GameState {

    private final static RelicRushPlugin MINI_GAME_PLUGIN = RelicRushPlugin.getInstance();

    public WaitingState(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onStart() {
        broadcast(ChatColor.GOLD + "Waiting for players... (" + plugin.getServer().getOnlinePlayers().size() + "/" + MINI_GAME_PLUGIN.getMinigameConfig().getMinPlayers() + ")");
        scheduleRepeating(() ->
                broadcast(ChatColor.GOLD + "Waiting for players... (" + plugin.getServer().getOnlinePlayers().size() + "/" + MINI_GAME_PLUGIN.getMinigameConfig().getMinPlayers() + ")"),
                20 * 5, 20 * 5);

        register(new BlockListener());
        register(new DamageListener());
    }

    @Override
    public void onUpdate() {}

    @Override
    public void onEnd() {}

    @Override
    public boolean isReadyToEnd() {
        return plugin.getServer().getOnlinePlayers().size() >= MINI_GAME_PLUGIN.getMinigameConfig().getMinPlayers();
    }

    @NotNull
    @Override
    public Duration getDuration() {
        return Duration.ZERO;
    }
}
