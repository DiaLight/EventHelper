package dialight.freezer;

import dialight.misc.player.UuidPlayer;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FreezeFlayers implements Listener {

    private final Plugin plugin;
    private final Map<UUID, UuidPlayer> flyers = new HashMap<>();
    private boolean enabled = false;

    public FreezeFlayers(Plugin plugin) {
        this.plugin = plugin;
    }

    public void enable() {
        if(enabled) return;
        PluginManager pluginManager = plugin.getServer().getPluginManager();
        pluginManager.registerEvents(this, plugin);
        enabled = true;
    }

    public void disable() {
        if(!enabled) return;
        HandlerList.unregisterAll(this);
        enabled = false;
    }

    public void setFly(UuidPlayer up) {
        flyers.put(up.getUuid(), up);
        up.setAllowFlight(true);
        up.setFlying(true);
    }

    public boolean isFlyingMode(GameMode gm) {
        return gm == GameMode.CREATIVE || gm == GameMode.SPECTATOR;
    }
    public void removeFly(UuidPlayer up) {
        flyers.remove(up.getUuid());
        if(isFlyingMode(up.getGameMode())) return;
        up.setAllowFlight(false);
        up.setFlying(false);
    }

    @EventHandler public void onJoin(PlayerJoinEvent e) {
        Player trg = e.getPlayer();
        if(trg.isOp()) return;
        if(isFlyingMode(trg.getGameMode())) return;
        if(flyers.containsKey(trg.getUniqueId())) {
            trg.setAllowFlight(true);
            trg.setFlying(true);
        } else {
            trg.setAllowFlight(false);
            trg.setFlying(false);
        }
    }
    @EventHandler public void onToggleFly(PlayerToggleFlightEvent e) {
        if(e.getPlayer().isOp()) return;
        if(isFlyingMode(e.getPlayer().getGameMode())) return;
        if(!e.isFlying()) {
            if(flyers.containsKey(e.getPlayer().getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }
    @EventHandler public void onPluginDisable(PluginDisableEvent e) {
        if(!e.getPlugin().getName().equals(plugin.getName())) return;
        for (UuidPlayer up : flyers.values()) {
            if(isFlyingMode(up.getGameMode())) continue;
            up.setAllowFlight(false);
            up.setFlying(false);
        }
        flyers.clear();
    }


}
