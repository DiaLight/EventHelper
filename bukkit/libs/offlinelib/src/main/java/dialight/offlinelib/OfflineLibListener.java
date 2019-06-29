package dialight.offlinelib;

import dialight.extensions.OfflinePlayerEx;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class OfflineLibListener implements Listener {

    private final OfflineLib proj;
    private final Server server;

    public OfflineLibListener(OfflineLib proj) {
        this.proj = proj;
        this.server = proj.getPlugin().getServer();
    }

    @EventHandler public void onPreLogin(AsyncPlayerPreLoginEvent e) {
        UUID uuid = e.getUniqueId();
        proj.runTask(() -> {
            OfflinePlayerEx opex = proj.get(uuid);
            if (opex != null) opex.save();
        });
    }

    @EventHandler public void onQuit(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        proj.runTask(() -> {
            OfflinePlayerEx opex = proj.getOrLoad(uuid);
            if (opex != null) opex.load();
        });
    }

}
