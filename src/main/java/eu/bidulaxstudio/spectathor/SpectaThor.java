package eu.bidulaxstudio.spectathor;

import eu.bidulaxstudio.spectathor.commands.Back;
import eu.bidulaxstudio.spectathor.commands.Spy;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class SpectaThor extends JavaPlugin {

    public Map<UUID, Stack<Location>> positions = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        registerCommands();
    }

    private void registerCommands() {
        getCommand("spy").setExecutor(new Spy(this));
        getCommand("back").setExecutor(new Back(this));
    }

    public void sendPluginMessage(CommandSender target, String message) {
        if (target instanceof Player player) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        } else {
            target.sendMessage(message);
        }
    }

    public void sendConfigMessage(CommandSender target, String configPath) {
        String message = getConfig().getString(configPath);
        if (!message.isEmpty()) sendPluginMessage(target, getConfig().getString(configPath));
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

            if (getConfig().getBoolean("settings.nightVision")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 255, false, false));
            }

            return true;

        } else {
            String unspyGamemode = getConfig().getString("settings.unspyGamemode").toUpperCase();
            GameMode gameMode;
            if (unspyGamemode.equalsIgnoreCase("default")) gameMode = getServer().getDefaultGameMode();
            else gameMode = GameMode.valueOf(unspyGamemode);

            player.setGameMode(gameMode);

            if (getConfig().getBoolean("settings.nightVision")) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            }

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
