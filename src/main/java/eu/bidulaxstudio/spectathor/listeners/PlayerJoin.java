package eu.bidulaxstudio.spectathor.listeners;

import eu.bidulaxstudio.spectathor.SpectaThor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    private final SpectaThor main;

    public PlayerJoin(SpectaThor main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("spectathor.autospy")) {
            main.invertPlayer(player, true);
        }
    }

}
