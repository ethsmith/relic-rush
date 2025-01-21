package dev.ethans.relicrush.commands;

import dev.ethans.relicrush.RelicRushPlugin;
import dev.ethans.relicrush.relic.RelicDeposit;
import dev.ethans.relicrush.state.waiting.WaitingState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

public class RelicDepositCommand implements CommandExecutor {

    private static final RelicRushPlugin plugin = RelicRushPlugin.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (!cmd.getName().equalsIgnoreCase("relicdeposit"))
            return true;

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "You must be a player to use this command.");
            return true;
        }

        if (!player.hasPermission("relicrush.relicdeposit")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return true;
        }

        if (!(plugin.getCurrentState() instanceof WaitingState)) {
            sender.sendMessage(ChatColor.RED + "The game has already started.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "Usage: /relicdeposit <team>");
            return true;
        }

        String teamName = args[0];
        RelicDeposit relicDeposit = new RelicDeposit(teamName, player.getLocation());
        plugin.getRelicManager().getRelicDeposits().put(teamName, relicDeposit);
        player.sendMessage(ChatColor.GREEN + "Relic spawn created!");

        try(FileWriter writer = new FileWriter(plugin.getDataFolder().getAbsolutePath() + "/deposits/" + UUID.randomUUID() + ".json")) {
            File file = new File(plugin.getDataFolder().getAbsolutePath() + "/deposits/");
            if (!file.exists())
                file.mkdirs();
            plugin.getGson().toJson(relicDeposit, writer);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save relic deposit to file.");
            e.printStackTrace();
        }

        return true;
    }
}
