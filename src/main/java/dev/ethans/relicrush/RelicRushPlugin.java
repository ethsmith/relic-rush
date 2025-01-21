package dev.ethans.relicrush;

import com.google.gson.Gson;
import dev.ethans.relicrush.commands.RelicDepositCommand;
import dev.ethans.relicrush.commands.RelicSpawnCommand;
import dev.ethans.relicrush.commands.ReloadCommand;
import dev.ethans.relicrush.config.MinigameConfig;
import dev.ethans.relicrush.relic.RelicManager;
import dev.ethans.relicrush.scoreboard.WaitingScoreboard;
import dev.ethans.relicrush.state.base.GameState;
import dev.ethans.relicrush.state.base.ScheduledStateSeries;
import dev.ethans.relicrush.state.ingame.InGameState;
import dev.ethans.relicrush.state.init.InitState;
import dev.ethans.relicrush.state.waiting.WaitingState;
import lombok.Getter;
import lombok.Setter;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import org.bukkit.plugin.java.JavaPlugin;

public final class RelicRushPlugin extends JavaPlugin {

    @Getter
    private static RelicRushPlugin instance;

    @Getter
    private MinigameConfig minigameConfig;

    @Getter
    private RelicManager relicManager;

    @Getter
    private ScheduledStateSeries fsm;

    @Getter
    private final Gson gson = new Gson();

    @Getter
    ScoreboardLibrary scoreboardLibrary;

    @Getter
    @Setter
    private GameState currentState;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        minigameConfig = new MinigameConfig();
        minigameConfig.load();
        relicManager = new RelicManager();
        relicManager.loadRelicSpawns();

        try {
            scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(this);
        } catch (NoPacketAdapterAvailableException e) {
            scoreboardLibrary = new NoopScoreboardLibrary();
            getLogger().severe("No scoreboard packet adapter available!");
            getServer().getPluginManager().disablePlugin(this);
        }

        WaitingScoreboard.createScoreboard();

        getCommand("relicreload").setExecutor(new ReloadCommand());
        getCommand("relicspawn").setExecutor(new RelicSpawnCommand());
        getCommand("relicdeposit").setExecutor(new RelicDepositCommand());

        fsm = new ScheduledStateSeries(this);
        fsm.add(new WaitingState(this));
        fsm.add(new InitState(this));
        fsm.add(new InGameState(this));
        fsm.start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        relicManager.getRelicSpawns().forEach(relicSpawn -> {
            if (relicSpawn.getSpawnedItem() != null)
                relicSpawn.getSpawnedItem().remove();
        });

        WaitingScoreboard.getSidebar().close();
        scoreboardLibrary.close();
    }
}