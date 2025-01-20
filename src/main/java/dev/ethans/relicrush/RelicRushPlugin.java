package dev.ethans.relicrush;

import com.google.gson.Gson;
import dev.ethans.relicrush.commands.RelicSpawnCommand;
import dev.ethans.relicrush.commands.ReloadCommand;
import dev.ethans.relicrush.config.MinigameConfig;
import dev.ethans.relicrush.relic.RelicManager;
import dev.ethans.relicrush.state.base.GameState;
import dev.ethans.relicrush.state.base.ScheduledStateSeries;
import dev.ethans.relicrush.state.ingame.InGameState;
import dev.ethans.relicrush.state.init.InitState;
import dev.ethans.relicrush.state.waiting.WaitingState;
import lombok.Getter;
import lombok.Setter;
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

        getCommand("minigamereload").setExecutor(new ReloadCommand());
        getCommand("relicspawn").setExecutor(new RelicSpawnCommand());

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
    }
}