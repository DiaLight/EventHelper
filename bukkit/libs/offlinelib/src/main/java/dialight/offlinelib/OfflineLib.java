package dialight.offlinelib;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.extensions.OfflinePlayerEx;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class OfflineLib extends Project {

    private final Map<UUID, OfflinePlayerEx> offlinePlayers = new HashMap<>();
    private final Map<UUID, UuidNotPlayer> notPlayers = new HashMap<>();

    private OfflineLibListener listener;
    private OnlineObservable online;
    private OfflineObservable offline;

    public OfflineLib(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void enable(EventHelper eh) {
        listener = new OfflineLibListener(this);
        online = new OnlineObservable(this);
        offline = new OfflineObservable(this);

        PluginManager pm = getPlugin().getServer().getPluginManager();
        pm.registerEvents(listener, getPlugin());
        pm.registerEvents(online, getPlugin());
        pm.registerEvents(offline, getPlugin());
    }

    @Override
    public void disable() {
        for (OfflinePlayerEx opex : offlinePlayers.values()) {
            opex.save();
        }
        HandlerList.unregisterAll(listener);
        HandlerList.unregisterAll(online);
        HandlerList.unregisterAll(offline);
        listener = null;
//        online = null;
//        offline = null;
    }

    @Override public ProjectApi getApi() {
        return new OfflineLibApi(this);
    }

    @Nullable public OfflinePlayerEx get(UUID uuid) {
        return offlinePlayers.get(uuid);
    }
    @Nullable public OfflinePlayerEx getOrLoad(UUID uuid) {
        OfflinePlayerEx opex = offlinePlayers.get(uuid);
        if(opex != null) return opex;
        opex = OfflinePlayerEx.of(getPlugin().getServer(), uuid);
        if(!opex.load()) return null;
        offlinePlayers.put(uuid, opex);
        return opex;
    }

    @Nullable public OfflinePlayerEx getOfflinePlayerExByEntity(Entity entity) {
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

    @Nullable public UuidPlayer getNotPlayer(UUID uuid) {
        return notPlayers.get(uuid);
    }
    @NotNull public UuidPlayer getUuidPlayer(UUID uuid) {
        UuidPlayer up = getNotPlayer(uuid);
        if(up != null) return up;
        return new UuidPlayer(this, uuid);
    }

    @NotNull public UuidPlayer getOrCreateNotPlayer(String name) {
        UuidNotPlayer unp = new UuidNotPlayer(this, name);
        notPlayers.put(unp.getUuid(), unp);
        return unp;
    }

    public Collection<UuidPlayer> getNotPlayers() {
        return (Collection) notPlayers.values();
    }
}
