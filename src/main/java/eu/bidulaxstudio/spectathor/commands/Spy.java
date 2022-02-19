package eu.bidulaxstudio.spectathor.commands;

import eu.bidulaxstudio.spectathor.SpectaThor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Spy implements CommandExecutor {

    private final SpectaThor main;

    public Spy(SpectaThor main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("spectathor.spy")) {
            main.sendConfigMessage(sender, "messages.noPermission");
            return true;
        }
        if (!(sender instanceof Player)) {
            main.sendConfigMessage(sender, "messages.notAPlayer");
            return true;
        }

        Player player = (Player) sender;

        if (main.spyPlayer(player)) {
            main.sendConfigMessage(player, "messages.spySuccess");
        } else {
            main.sendConfigMessage(player, "messages.unspySuccess");
        }

        return true;
    }

}
