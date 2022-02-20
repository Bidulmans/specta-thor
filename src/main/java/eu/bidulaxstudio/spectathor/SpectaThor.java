package eu.bidulaxstudio.spectathor;

import eu.bidulaxstudio.spectathor.commands.Back;
import eu.bidulaxstudio.spectathor.commands.Spy;
import eu.bidulaxstudio.spectathor.listeners.SessionEvent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class SpectaThor extends JavaPlugin {

    public Map<UUID, Stack<Location>> positions = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerCommands();
        registerEvents();
    }

    private void registerCommands() {
        getCommand("spy").setExecutor(new Spy(this));
        getCommand("back").setExecutor(new Back(this));
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new SessionEvent(this), this);
    }

    public void sendPluginMessage(CommandSender target, String message) {
        target.sendMessage(ChatColor.GOLD + "SpectaThor: " + ChatColor.WHITE + message);
    }

    public void sendConfigMessage(CommandSender target, String configPath) {
        String message = getConfig().getString(configPath);
        if (!message.equals("null")) sendPluginMessage(target, getConfig().getString(configPath));
    }

    public void savePosition(Player player) {
        UUID uuid = player.getUniqueId();
        Stack<Location> playerPositions;

        if (positions.get(uuid) != null) {
            playerPositions = positions.get(uuid);
        } else {
            playerPositions = new Stack<>();
        }

        playerPositions.add(player.getLocation());

        positions.put(uuid, playerPositions);
    }

    public boolean spyPlayer(Player player) {
        if (player.getGameMode() != GameMode.SPECTATOR) {
            player.setGameMode(GameMode.SPECTATOR);
            savePosition(player);
            return true;
        } else {
            player.setGameMode(getServer().getDefaultGameMode());
            return false;
        }
    }

    public boolean backPosition(Player player) {
        UUID uuid = player.getUniqueId();
        if (positions.get(uuid) != null) {
            Stack<Location> playerPositions = positions.get(uuid);

            Location position;
            if (playerPositions.size() != 0) {
                position = playerPositions.pop();
            } else {
                return false;
            }

            positions.put(uuid, playerPositions);

            player.teleport(position);
            return true;
        } else {
            return false;
        }
    }

}
