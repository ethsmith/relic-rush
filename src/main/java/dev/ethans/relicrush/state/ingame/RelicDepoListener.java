package dev.ethans.relicrush.state.ingame;

import dev.ethans.relicrush.RelicRushPlugin;
import dev.ethans.relicrush.relic.Relic;
import dev.ethans.relicrush.team.Team;
import dev.ethans.relicrush.team.TeamManager;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class RelicDepoListener implements Listener {

    private static final RelicRushPlugin plugin = RelicRushPlugin.getInstance();

    @EventHandler
    public void onRelicDepo(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Team team = TeamManager.getTeam(player);

        if (team == null)   return;

        // TODO Check if player is in their team relic depo

        player.getInventory().forEach(item -> {
            if (item.getItemMeta() == null) return;

            if (!item.getItemMeta().getPersistentDataContainer()
                    .has(new NamespacedKey(plugin, "relic"))) return;

            if (Relic.GOLDEN.getItem().isSimilar(item)) return;

            player.getInventory().remove(item);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

            if (Relic.RARE.getItem().isSimilar(item))
                team.setScore(team.getScore() + plugin.getMinigameConfig().getRareRelicScore());

            if (Relic.COMMON.getItem().isSimilar(item))
                team.setScore(team.getScore() + plugin.getMinigameConfig().getCommonRelicScore());
        });

        player.sendMessage(ChatColor.GREEN + "Relic(s) deposited for your team!");
    }
}
