package dev.ethans.relicrush.team;

import dev.ethans.relicrush.RelicRushPlugin;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamManager {

    private static final RelicRushPlugin plugin = RelicRushPlugin.getInstance();

    @Getter
    public static List<Team> teams = new ArrayList<>();

    public static void createTeams() {
        int teamCount = plugin.getMinigameConfig().getTeams();

        // Split players equally into teamCount amount of teams
        for (int i = 0; i < teamCount; i++) {
            Team team = new Team("Team " + (i + 1), new ArrayList<>());
            teams.add(team);

            Location spawnLocation = new Location(plugin.getServer().getWorld(plugin.getConfig().getString("Spawns." + (i + 1) + ".World")),
                    plugin.getConfig().getDouble("Spawns." + (i + 1) + ".X"), plugin.getConfig().getDouble("Spawns." + (i + 1) + ".Y"),
                    plugin.getConfig().getDouble("Spawns." + (i + 1) + ".Z"));

            team.setSpawnLocation(spawnLocation);
        }

        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            Team team = teams.get(i % teamCount);
            team.getPlayers().add(player);
        }

        // Check if every team has at least one player
        if (teams.stream().anyMatch(team -> team.getPlayers().isEmpty())) {
            plugin.getLogger().severe("Every team must have at least one player! Reduce the amount of teams");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }

        plugin.getLogger().info("Teams created!");
    }

    public static void loadTeams() {
        for (int teamNum : plugin.getMinigameConfig().getTeamPlayers().keySet()) {
            List<String> playerUuids = plugin.getMinigameConfig().getTeamPlayers().get(teamNum);
            Team team = new Team(plugin.getConfig().getString("Teams." + teamNum + ".Name"), new ArrayList<>());
            team.setSpawnLocation(plugin.getConfig().getLocation("Teams." + teamNum + ".Spawn"));

            for (String playerUuid : playerUuids) {
                UUID uuid = UUID.fromString(playerUuid);
                Player player = plugin.getServer().getPlayer(uuid);

                if (player == null)
                    plugin.getLogger().severe("Player with UUID " + playerUuid + " is not online!");

                team.getPlayers().add(player);
            }

            teams.add(team);
        }
    }

    public static Team getTeam(Player player) {
        return teams.stream().filter(team -> team.getPlayers().contains(player)).findFirst().orElse(null);
    }

    public static boolean teamExists(String name) {
        return teams.stream().anyMatch(team -> team.getName().equalsIgnoreCase(name));
    }
}
