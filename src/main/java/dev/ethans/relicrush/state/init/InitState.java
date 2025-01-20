package dev.ethans.relicrush.state.init;

import dev.ethans.relicrush.RelicRushPlugin;
import dev.ethans.relicrush.state.base.GameState;
import dev.ethans.relicrush.team.TeamManager;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public class InitState extends GameState {

    private static final RelicRushPlugin MINI_GAME_PLUGIN = RelicRushPlugin.getInstance();

    private boolean readyToEnd = false;

    public InitState(JavaPlugin plugin) {
        super(plugin);
    }

    @NotNull
    @Override
    public Duration getDuration() {
        return Duration.ZERO;
    }

    @Override
    protected void onStart() {
        if (MINI_GAME_PLUGIN.getMinigameConfig().isRandomTeams())
            TeamManager.createTeams();
        else
            TeamManager.loadTeams();

        TeamManager.getTeams().forEach(team -> {
            team.getPlayers().forEach(player -> {
                player.sendMessage("You are on team " + team.getName());
            });

            plugin.getLogger().info("Team " + team.getName() + " has " + team.getPlayers().size() + " players!");
        });

        // Spawn Relics
        MINI_GAME_PLUGIN.getRelicManager().getRelicSpawns().forEach(relicSpawn -> {
            ItemStack relic = relicSpawn.getType().getItem();

            Location relicLocation = relicSpawn.getLocation();
            relicLocation.setY(relicLocation.getBlockY() + 0.5);

            Item item = relicSpawn.getLocation().getWorld().dropItem(relicLocation, relic);
            item.setVelocity(item.getVelocity().zero());
            item.setGravity(false);
            item.setGlowing(true);
//            item.setPickupDelay(Integer.MAX_VALUE);

            relicSpawn.setSpawnedItem(item);
            plugin.getLogger().info("Spawned relic " + relicSpawn.getType().name() + " of type " + relicSpawn.getType().getItem().getType().name() + " at " + relicSpawn.getLocation());
        });

        readyToEnd = true;
    }

    @Override
    public void onUpdate() {

    }

    @Override
    protected void onEnd() {

    }

    @Override
    public boolean isReadyToEnd() {
        return readyToEnd;
    }
}
