package dev.ethans.relicrush.commands;

import dev.ethans.relicrush.RelicRushPlugin;
import dev.ethans.relicrush.relic.Relic;
import dev.ethans.relicrush.relic.RelicSpawn;
import dev.ethans.relicrush.state.waiting.WaitingState;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.UUID;

public class RelicSpawnCommand implements CommandExecutor {

    private final static RelicRushPlugin plugin = RelicRushPlugin.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!cmd.getName().equalsIgnoreCase("relicspawn"))
            return true;

        if (!sender.hasPermission("relicrush.relicspawn")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /relicspawn <relic>");
            return true;
        }

        if (Arrays.stream(Relic.values()).noneMatch(relic -> relic.name().equalsIgnoreCase(args[0].toUpperCase()))) {
            sender.sendMessage(ChatColor.RED + "Invalid relic type.");
            return true;
        }

        if (!(plugin.getCurrentState() instanceof WaitingState)) {
            sender.sendMessage(ChatColor.RED + "The game has already started.");
            return true;
        }

        Location location = player.getLocation();
        // Make location center of block
        location.setX(location.getBlockX() + 0.5);
        location.setZ(location.getBlockZ() + 0.5);

        RelicSpawn relicSpawn = new RelicSpawn(Relic.valueOf(args[0].toUpperCase()), location);
        plugin.getRelicManager().getRelicSpawns().add(relicSpawn);
        player.sendMessage(ChatColor.GREEN + "Relic spawn created!");

        FileWriter writer = null;
        try {
            File file = new File(plugin.getDataFolder().getAbsolutePath() + "/relics/");
            if (!file.exists())
                file.mkdirs();

            writer = new FileWriter(plugin.getDataFolder().getAbsolutePath() + "/relics/" + UUID.randomUUID() + ".json");
            plugin.getGson().toJson(relicSpawn, writer);
            writer.close();
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save relic spawn to file.");
            e.printStackTrace();
        }

        return true;
    }
}
