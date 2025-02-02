package dev.ethans.relicrush.state.ingame;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class RelicDamageListener implements Listener {

    // TODO This implementation needs to be better, temporary to continue testing other features
    @EventHandler
    public void onRelicDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Item item)) return;

        List<Material> relicMaterials = List.of(Material.TOTEM_OF_UNDYING, Material.EMERALD, Material.GOLD_INGOT);
        if (!relicMaterials.contains(item.getItemStack().getType())) return;

        event.setCancelled(true);
    }
}
