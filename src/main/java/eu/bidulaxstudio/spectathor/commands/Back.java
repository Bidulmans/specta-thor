package eu.bidulaxstudio.spectathor.commands;

import eu.bidulaxstudio.spectathor.SpectaThor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Back implements CommandExecutor {

    private final SpectaThor plugin;

    public Back(SpectaThor plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.sendConfigMessage(sender, "messages.not-a-player");
            return true;
        }

        if (plugin.backPosition(player)) {
            plugin.sendConfigMessage(player, "messages.back-success");
        } else {
            plugin.sendConfigMessage(player, "messages.back-fail");
        }

        return true;
    }

}
