package eu.bidulaxstudio.spectathor.commands;

import eu.bidulaxstudio.spectathor.SpectaThor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class Spy implements CommandExecutor, TabCompleter {

    private final SpectaThor plugin;

    public Spy(SpectaThor plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            plugin.sendConfigMessage(sender, "messages.not-a-player");
            return true;
        }

        if (args.length > 0 && plugin.getConfig().getBoolean("settings.spy-players")) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                plugin.sendPluginMessage(player, plugin.getConfigString("messages.spy-player-fail").replace("{PLAYER}", args[0]));
                return true;
            }

            plugin.invertPlayer(player, true);
            player.setSpectatorTarget(target);

            plugin.sendPluginMessage(player, plugin.getConfigString("messages.spy-player-success").replace("{PLAYER}", target.getName()));
            return true;
        }

        if (plugin.invertPlayer(player, false)) {
            plugin.sendConfigMessage(player, "messages.spy-success");

        } else {
            plugin.sendConfigMessage(player, "messages.unspy-success");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && plugin.getConfig().getBoolean("settings.spy-players")) {
            return null;
        }

        return Collections.emptyList();
    }

}
