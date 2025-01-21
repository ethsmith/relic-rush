package dev.ethans.relicrush.relic;

import dev.ethans.relicrush.RelicRushPlugin;
import lombok.Getter;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelicManager {

    private static final RelicRushPlugin plugin = RelicRushPlugin.getInstance();

    @Getter
    private final List<RelicSpawn> relicSpawns = new ArrayList<>();

    @Getter
    private final Map<String, RelicDeposit> relicDeposits = new HashMap<>();

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

    public void loadRelicDeposits() {
        File file = new File(plugin.getDataFolder().getAbsolutePath() + "/deposits/");
        if (!file.exists()) return;

        for (File depositFile : file.listFiles()) {
            try (FileReader reader = new FileReader(depositFile)) {
                if (depositFile.isDirectory()) continue;
                if (!depositFile.getName().endsWith(".json")) continue;

                RelicDeposit relicDeposit = plugin.getGson().fromJson(reader, RelicDeposit.class);
                relicDeposit.deserialize();
                relicDeposits.put(relicDeposit.getTeamName(), relicDeposit);

                plugin.getLogger().info("Loaded relic deposit from file " + depositFile.getName());
                plugin.getLogger().info("Location: " + relicDeposit.getCenter().toString());
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to load relic deposit from file " + depositFile.getName());
                e.printStackTrace();
            }
        }
    }
}
