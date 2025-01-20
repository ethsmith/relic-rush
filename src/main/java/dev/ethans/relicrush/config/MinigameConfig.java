package dev.ethans.relicrush.config;

import dev.ethans.relicrush.RelicRushPlugin;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinigameConfig {

    private final static RelicRushPlugin plugin = RelicRushPlugin.getInstance();

    @Getter
    private int maxPlayers;
    @Getter
    private int minPlayers;
    @Getter
    private int teams;
    @Getter
    private int gameDurationInMinutes;
    @Getter
    private int goldenRelicSecondsToWin;
    @Getter
    private int commonRelicScore;
    @Getter
    private int rareRelicScore;
    @Getter
    private int maxRelicsPerPlayer;
    @Getter
    private int respawnDelay;

    @Getter
    private boolean randomTeams;
    @Getter
    private final Map<Integer, List<String>> teamPlayers = new HashMap<>();

    public void load() {
        // Load config
        plugin.saveDefaultConfig();
        reload();
    }

    public void reload() {
        maxPlayers = plugin.getConfig().getInt("General.Max_Players");
        minPlayers = plugin.getConfig().getInt("General.Min_Players");
        teams = plugin.getConfig().getInt("General.Teams");
        gameDurationInMinutes = plugin.getConfig().getInt("General.Game_Duration");
        goldenRelicSecondsToWin = plugin.getConfig().getInt("General.Golden_Relic_Seconds_To_Win");
        commonRelicScore = plugin.getConfig().getInt("General.Common_Relic_Score");
        rareRelicScore = plugin.getConfig().getInt("General.Rare_Relic_Score");
        maxRelicsPerPlayer = plugin.getConfig().getInt("General.Max_Relics_Per_Player");
        respawnDelay = plugin.getConfig().getInt("General.Respawn_Delay");

        randomTeams = plugin.getConfig().getBoolean("Teams.Random");

        if (randomTeams) {
            int i = 1;
            while (plugin.getConfig().contains("Teams." + i)) {
                teamPlayers.put(i, plugin.getConfig().getStringList("Teams." + i + ".Players"));
                i++;
            }
        }
    }
}
