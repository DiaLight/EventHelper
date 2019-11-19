package dialight.extensions.packet;

import dialight.nms.PlayerNms;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class BroadcastPacketListener implements Listener {

    @NotNull private final Server server;
    @NotNull private final JavaPlugin plugin;
    @NotNull private final String id;

    @Nullable private PacketHandler handler = null;

    public BroadcastPacketListener(JavaPlugin plugin, String id) {
        this.server = plugin.getServer();
        this.plugin = plugin;
        this.id = id;
    }

    public void onInboundPacket(PacketEvent e) {}
    public void onOutboundPacket(PacketEvent e) {}

    private void injectTo(Player player) {
        this.handler = PlayerNms.of(player).getOrCreatePacketHandler(id);
        this.handler.onInboundPacket(this::onInboundPacket);
        this.handler.onOutboundPacket(this::onOutboundPacket);
    }

    @Nullable public Player nextOnline(@Nullable Player exclude) {
        Collection<? extends Player> players = server.getOnlinePlayers();
        for (Player pl : players) {
            if (exclude != null && exclude.getUniqueId().equals(pl.getUniqueId())) continue;
            return pl;
        }
        return null;
    }

    public void onListenerAttach(Player player) {}
    public void onListenerDetach(Player player) {}

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if(handler == null) return;
        if (!handler.getPlayer().getUniqueId().equals(e.getPlayer().getUniqueId())) return;
        onListenerDetach(e.getPlayer());
        handler.unregister();
        Player nextPlayer = nextOnline(e.getPlayer());
        injectTo(nextPlayer);
    }

    public boolean start() {
        PluginManager pm = server.getPluginManager();
        pm.registerEvents(this, plugin);
        Player nextHost = nextOnline(null);
        if(nextHost != null) {
            injectTo(nextHost);
            return true;
        }
        return false;
    }

    public void stop() {
        HandlerList.unregisterAll(this);
        if(handler != null) handler.unregister();
    }

}
