package dev.ethans.relicrush.state.ingame;

import dev.ethans.relicrush.RelicRushPlugin;
import dev.ethans.relicrush.relic.Relic;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ItemPickupListener implements Listener {

    private static final RelicRushPlugin plugin = RelicRushPlugin.getInstance();

    private final InGameState inGameState;

    public ItemPickupListener(InGameState inGameState) {
        this.inGameState = inGameState;
    }

    @EventHandler
    public void onRelicPickup(PlayerPickupItemEvent event) {
        ItemStack itemStack = event.getItem().getItemStack();
        Player player = event.getPlayer();

        if (itemStack.getItemMeta() == null) return;

        if (!itemStack.getItemMeta().getPersistentDataContainer()
                .has(new NamespacedKey(plugin, "relic"))) return;

        if (Relic.GOLDEN.getItem().isSimilar(itemStack)) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            player.setGlowing(true);
            plugin.getServer().broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.GREEN + " picked up the " + ChatColor.GOLD + "GOLDEN" + ChatColor.GREEN + " Relic!");

            inGameState.setPlayerWithRelic(player);
            inGameState.setGoldenRelicCountdown(plugin.getMinigameConfig().getGoldenRelicSecondsToWin());
            return;
        }
    }
}
