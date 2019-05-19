package dialight.offlinelib;

import dialight.eventhelper.EventHelper;
import dialight.eventhelper.project.Project;
import dialight.eventhelper.project.ProjectApi;
import dialight.extensions.OfflinePlayerEx;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OfflineLib extends Project {

    private final Map<UUID, OfflinePlayerEx> offlinePlayers = new HashMap<>();

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

    @Override
    public ProjectApi getApi() {
        return new OfflineLibApi(this);
    }

    @Nullable public OfflinePlayerEx get(UUID uuid) {
        return offlinePlayers.get(uuid);
    }
    @NotNull public OfflinePlayerEx getOrCreate(UUID uuid) {
        OfflinePlayerEx opex = offlinePlayers.get(uuid);
        if(opex != null) return opex;
        getPlugin().getServer().getOfflinePlayer(uuid);
        opex = new OfflinePlayerEx(getPlugin().getServer(), uuid);
        offlinePlayers.put(uuid, opex);
        return opex;
    }

    @Nullable public OfflinePlayerEx getOfflinePlayerExByEntity(Entity entity) {
        return null;
    }

    public OnlineObservable getOnline() {
        return online;
    }

    public OfflineObservable getOffline() {
        return offline;
    }

}
