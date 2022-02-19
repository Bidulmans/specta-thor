package eu.bidulaxstudio.spectathor.listeners;

import eu.bidulaxstudio.spectathor.SpectaThor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class SessionEvent implements Listener {

    private final SpectaThor main;

    public SessionEvent(SpectaThor main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (player.hasPermission("spectathor.spy") && main.positions.get(uuid) != null) {
            main.positions.remove(uuid);
        }
    }

}
