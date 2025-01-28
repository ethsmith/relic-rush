package dev.ethans.relicrush.state.ingame;

import dev.ethans.relicrush.RelicRushPlugin;
import dev.ethans.relicrush.messages.BossBarMessage;
import dev.ethans.relicrush.state.base.GameState;
import dev.ethans.relicrush.team.Team;
import dev.ethans.relicrush.team.TeamManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.Comparator;

public class InGameState extends GameState {

    private final static RelicRushPlugin MINI_GAME_PLUGIN = RelicRushPlugin.getInstance();

    @Getter
    @Setter
    private int goldenRelicCountdown = MINI_GAME_PLUGIN.getMinigameConfig().getGoldenRelicSecondsToWin();

    @Getter
    @Setter
    private Player playerWithRelic= null;
    private boolean goldenRelicWin = false;

    public InGameState(JavaPlugin plugin) {
        super(plugin);
    }

    @NotNull
    @Override
    public Duration getDuration() {
        return Duration.ofMinutes(MINI_GAME_PLUGIN.getMinigameConfig().getGameDurationInMinutes());
    }

    @Override
    protected void onStart() {
        register(new PlayerDeathListener(this));
        register(new ItemPickupListener(this));
        register(new RelicDepositListener());

        TeamManager.getTeams().forEach(team -> {
            team.getPlayers().forEach(player -> {
                player.teleport(team.getSpawnLocation());
                player.sendMessage(ChatColor.GREEN + "The game has started!");
                BossBarMessage.sendTimer(player, ChatColor.GOLD + "{time} left to win the game!", (int) getDuration().getSeconds());
            });
        });

        scheduleRepeating(() -> {
            if (playerWithRelic == null) {
                goldenRelicCountdown = MINI_GAME_PLUGIN.getMinigameConfig().getGoldenRelicSecondsToWin();
                return;
            }

            goldenRelicCountdown--;

            if (goldenRelicCountdown <= 0) {
                goldenRelicWin = true;
                end();
            }
        }, 20, 20);
    }

    @Override
    public void onUpdate() {

    }

    @Override
    protected void onEnd() {
        if (goldenRelicWin) {
            broadcast(ChatColor.GREEN + "The game has ended! " + TeamManager.getTeam(playerWithRelic).getName() + " has won with the golden relic!");
        } else {
            // TODO Using this for a small community even so not too worried about a tie, but can be handled later
            Team teamWithMostPoints = TeamManager.getTeams().stream().max(Comparator.comparingInt(Team::getScore)).orElse(null);

            if (teamWithMostPoints == null) {
                broadcast(ChatColor.RED + "The game has ended! No team has won.");
                return;
            }

            broadcast(ChatColor.GREEN + "The game has ended! " + teamWithMostPoints.getName() + " has won with " + teamWithMostPoints.getScore() + " points!");
        }
    }
}
