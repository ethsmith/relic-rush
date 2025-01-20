package dev.ethans.relicrush.relic;

import dev.ethans.relicrush.RelicRushPlugin;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public enum Relic {

    COMMON(Material.GOLD_INGOT, ChatColor.YELLOW + "Common Relic", List.of(
            ChatColor.GRAY + "A common relic found in the world...",
            ChatColor.GRAY + "Deposit this at your base for " + RelicRushPlugin.getInstance().getMinigameConfig().getCommonRelicScore() + " points!"
    )),

    RARE(Material.EMERALD, ChatColor.GREEN + "Rare Relic", List.of(
            ChatColor.GRAY + "A rare relic worshipped by villagers...",
            ChatColor.GRAY + "Deposit this at your base for " + RelicRushPlugin.getInstance().getMinigameConfig().getRareRelicScore() + " points!"
    )),

    GOLDEN(Material.TOTEM_OF_UNDYING, ChatColor.GOLD + "Golden Relic", List.of(
            ChatColor.GRAY + "A relic of unimaginable power...",
            ChatColor.GRAY + "Hold it for " + RelicRushPlugin.getInstance().getMinigameConfig().getGoldenRelicSecondsToWin() + " seconds to win the game!"
    ));

    @Getter
    private final ItemStack item;

    Relic(Material item, String name, List<String> description) {
        ItemStack relic  = new ItemStack(item);
        ItemMeta relicMeta = relic.getItemMeta();

        relicMeta.setDisplayName(name);
        relicMeta.setLore(description);
        relicMeta.setUnbreakable(true);
        relicMeta.getPersistentDataContainer().set(new NamespacedKey(RelicRushPlugin.getInstance(), "relic"), PersistentDataType.BOOLEAN, true);

        relic.setItemMeta(relicMeta);
        this.item = relic;
    }
}
