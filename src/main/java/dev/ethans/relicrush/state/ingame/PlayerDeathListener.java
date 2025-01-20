package dev.ethans.relicrush.state.ingame;

import dev.ethans.relicrush.RelicRushPlugin;
import dev.ethans.relicrush.team.TeamManager;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDeathListener implements Listener {

    private static final RelicRushPlugin plugin = RelicRushPlugin.getInstance();

    private final InGameState inGameState;

    public PlayerDeathListener(InGameState inGameState) {
        this.inGameState = inGameState;
    }

    @EventHandler
    public void onPlayerDeath(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

        if (event.getDamage() < player.getHealth())
            return;

        event.setCancelled(true);

        // Drop relics
        player.getInventory().forEach(item -> {
            if (item == null) return;
            if (item.getItemMeta() != null && item.getItemMeta().getPersistentDataContainer()
                    .has(new NamespacedKey(RelicRushPlugin.getInstance(), "relic"))) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
                player.setGlowing(false);
            }
        });

        // Set player to spectator
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SPECTATOR);

        // Start respawn timer
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(TeamManager.getTeam(player).getSpawnLocation());
        }, 20L * plugin.getMinigameConfig().getRespawnDelay());
    }
}
