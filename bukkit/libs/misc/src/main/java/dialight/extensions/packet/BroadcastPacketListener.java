package dialight.extensions.packet;

import dialight.nms.PlayerNms;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public abstract class BroadcastPacketListener implements Listener {

    private final Server server;
    private final String id;
    @Nullable
    private PacketHandler handler = null;

    public BroadcastPacketListener(Server server, String id) {
        this.server = server;
        this.id = id;
    }

    public abstract void onInboundPacket(PacketEvent e);
    public abstract void onOutboundPacket(PacketEvent e);

    private void injectTo(Player player) {
        this.handler = PlayerNms.of(player).getOrCreatePacketHandler(id);
        this.handler.onInboundPacket(this::onInboundPacket);
        this.handler.onOutboundPacket(this::onOutboundPacket);
    }

    private boolean injectOne(UUID... exclude) {
        if(handler != null) return false;
        Collection<? extends Player> players = server.getOnlinePlayers();
        List<UUID> excludeList = Arrays.asList(exclude);
        for (Player player : players) {
            if (excludeList.contains(player.getUniqueId())) continue;
            injectTo(player);
            return true;
        }
        return false;
    }

    public abstract void onListenerAttach();
    public abstract void onListenerDetach();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if(handler != null) return;
        injectTo(e.getPlayer());
        onListenerAttach();
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if(handler == null) return;
        if (!handler.getPlayer().getUniqueId().equals(e.getPlayer().getUniqueId())) return;
        handler.unregister();
        handler = null;
        if(!injectOne(e.getPlayer().getUniqueId())) {
            onListenerDetach();
        }
    }

    public void start(JavaPlugin plugin) {
        PluginManager pm = server.getPluginManager();
        pm.registerEvents(this, plugin);
        injectOne();
    }

    public void stop() {
        HandlerList.unregisterAll(this);
        if(handler != null) handler.unregister();
    }

}
