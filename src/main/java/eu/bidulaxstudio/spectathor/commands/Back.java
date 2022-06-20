package eu.bidulaxstudio.spectathor.commands;

import eu.bidulaxstudio.spectathor.SpectaThor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Back implements CommandExecutor {

    private final SpectaThor main;

    public Back(SpectaThor main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            main.sendConfigMessage(sender, "messages.notAPlayer");
            return true;
        }

        if (main.backPosition(player)) {
            main.sendConfigMessage(player, "messages.backSuccess");
        } else {
            main.sendConfigMessage(player, "messages.backFail");
        }

        return true;
    }

}
