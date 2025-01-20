package dev.ethans.relicrush.relic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Item;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class RelicSpawn {

    private transient Relic type;
    private transient Location location;
    private transient Item spawnedItem;

    private String typeName;
    private Map<String, Object> serializedLocation;

    public RelicSpawn(Relic type, Location location) {
        this.type = type;
        this.location = location;
        this.serializedLocation = location.serialize();
        this.typeName = type.name();
    }

    public void deserialize() {
        this.location = Location.deserialize(serializedLocation);
        this.type = Relic.valueOf(typeName);
    }
}
