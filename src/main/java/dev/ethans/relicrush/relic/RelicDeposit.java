package dev.ethans.relicrush.relic;

import dev.ethans.relicrush.RelicRushPlugin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;

import java.util.Map;

@NoArgsConstructor
public class RelicDeposit {

    private static final RelicRushPlugin plugin = RelicRushPlugin.getInstance();

    @Getter
    @Setter
    private String teamName;

    @Getter
    @Setter
    private transient Location center;

    @Getter
    @Setter
    private Map<String, Object> serializedLocation;

    public RelicDeposit(String name, Location center) {
        this.teamName = name;
        this.center = new Location(center.getWorld(), center.getBlockX() + 0.5, center.getBlockY(), center.getBlockZ() + 0.5);
        this.serializedLocation = center.serialize();
    }

    public void deserialize() {
        center = Location.deserialize(serializedLocation);
    }
}
