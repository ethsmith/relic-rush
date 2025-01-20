package dev.ethans.relicrush.commands;

import dev.ethans.relicrush.RelicRushPlugin;
import dev.ethans.relicrush.state.waiting.WaitingState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {

    private final static RelicRushPlugin plugin = RelicRushPlugin.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("minigamereload"))
            return true;

        if (!sender.hasPermission("minigame.reload")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (!(plugin.getCurrentState() instanceof WaitingState)) {
            sender.sendMessage(ChatColor.RED + "The game has already started.");
            return true;
        }

        plugin.getMinigameConfig().reload();
        sender.sendMessage(ChatColor.GREEN + "Reloaded config!");
        return true;
    }
}
