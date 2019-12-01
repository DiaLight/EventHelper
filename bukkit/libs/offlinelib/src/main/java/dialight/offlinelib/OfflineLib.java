package dialight.offlinelib;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.misc.player.PlayerEngine;
import dialight.misc.player.UuidPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class OfflineLib extends Project {

    private PlayerEngine playerEngine;
    private OnlineObservable online;
    private OfflineObservable offline;

    public OfflineLib(JavaPlugin plugin) {
        super(plugin);
    }

    @Override public void enable(EventHelper eh) {
        playerEngine = new PlayerEngine(getPlugin());
        online = new OnlineObservable(this);
        offline = new OfflineObservable(this);

        playerEngine.enable();
        PluginManager pm = getPlugin().getServer().getPluginManager();
        pm.registerEvents(online, getPlugin());
        pm.registerEvents(offline, getPlugin());
    }

    @Override public void disable() {
        playerEngine.disable();
        HandlerList.unregisterAll(online);
        HandlerList.unregisterAll(offline);
        playerEngine = null;
//        online = null;
//        offline = null;
    }

    @Override public ProjectApi getApi() {
        return new OfflineLibApi(this);
    }

    @Nullable public UuidPlayer getUuidPlayerExByEntity(Entity entity) {
        // TODO: parse specific entity player representation (Villager?)
        return null;
    }

    public OnlineObservable getOnline() {
        return online;
    }

    public OfflineObservable getOffline() {
        return offline;
    }

    @Nullable public OfflinePlayer getOfflinePlayerByName(String name) {
        Server server = getPlugin().getServer();
        for (OfflinePlayer op : server.getOfflinePlayers()) {
            if(Objects.equals(op.getName(), name)) return op;
        }
        return null;
    }

    @NotNull public UuidPlayer getUuidPlayer(UUID uuid) {
        return playerEngine.getOrLoad(uuid);
    }

    public PlayerEngine getPlayerEngine() {
        return playerEngine;
    }

    public UuidPlayer getUuidPlayer(String name) {
        return playerEngine.getOrLoad(name);
    }

}
