package eu.bidulaxstudio.spectathor;

import eu.bidulaxstudio.spectathor.commands.Back;
import eu.bidulaxstudio.spectathor.commands.Spy;
import eu.bidulaxstudio.spectathor.listeners.AchievementDone;
import eu.bidulaxstudio.spectathor.listeners.PlayerJoin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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
        registerListeners();
    }

    private void registerCommands() {
        getCommand("spy").setExecutor(new Spy(this));
        getCommand("back").setExecutor(new Back(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);
        if (getConfig().getBoolean("settings.hide-achievements")) {
            getServer().getPluginManager().registerEvents(new AchievementDone(), this);
        }
    }

    public void sendPluginMessage(CommandSender target, String message) {
        if (target instanceof Player player) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        } else {
            target.sendMessage(message);
        }
    }

    public void sendConfigMessage(CommandSender target, String path) {
        String message = getConfigString(path);
        if (!message.isEmpty()) {
            sendPluginMessage(target, message);
        }
    }

    public String getConfigString(String path) {
        String message = getConfig().getString(path);
        return message == null ? "" : message;
    }

    public boolean invertPlayer(Player player, boolean forceSpy) {
        // Spy player
        if (player.getGameMode() != GameMode.SPECTATOR || forceSpy) {
            player.setGameMode(GameMode.SPECTATOR);

            savePosition(player);

            if (getConfig().getBoolean("settings.night-vision")) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 1000000, 255, false, false));
            }

            return true;
        // Unspy player
        } else {
            String unspyGameMode = getConfig().getString("settings.unspy-gamemode").toUpperCase();
            GameMode gameMode;
            if (unspyGameMode.equalsIgnoreCase("default")) gameMode = getServer().getDefaultGameMode();
            else gameMode = GameMode.valueOf(unspyGameMode);

            player.setGameMode(gameMode);

            if (getConfig().getBoolean("settings.night-vision")) {
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            }

            return false;
        }
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
