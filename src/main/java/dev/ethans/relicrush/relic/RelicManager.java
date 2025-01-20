package dev.ethans.relicrush.relic;

import dev.ethans.relicrush.RelicRushPlugin;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class RelicManager {

    private static final RelicRushPlugin plugin = RelicRushPlugin.getInstance();

    @Getter
    private final List<RelicSpawn> relicSpawns = new ArrayList<>();

    public void loadRelicSpawns() {
        File file = new File(plugin.getDataFolder().getAbsolutePath() + "/relics/");
        if (!file.exists()) return;

        for (File relicFile : file.listFiles()) {
            try (FileReader reader = new FileReader(relicFile)) {
                if (relicFile.isDirectory()) continue;
                if (!relicFile.getName().endsWith(".json")) continue;

                RelicSpawn relicSpawn = plugin.getGson().fromJson(reader, RelicSpawn.class);
                relicSpawn.deserialize();
                relicSpawns.add(relicSpawn);

                plugin.getLogger().info("Loaded relic spawn from file " + relicFile.getName());
                plugin.getLogger().info("Relic type: " + relicSpawn.getType().name());
                plugin.getLogger().info("Location: " + relicSpawn.getLocation().toString());
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to load relic spawn from file " + relicFile.getName());
                e.printStackTrace();
            }
        }
    }
}
