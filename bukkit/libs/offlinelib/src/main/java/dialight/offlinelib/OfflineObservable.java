package dialight.offlinelib;

import dialight.misc.player.UuidPlayer;
import dialight.observable.collection.ObservableCollection;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class OfflineObservable extends ObservableCollection<UuidPlayer> implements Listener {

    private final Server srv;
    private final Map<UUID, UuidPlayer> offlineMap = new HashMap<>();
    private final OfflineLib proj;

    public OfflineObservable(OfflineLib proj) {
        this.proj = proj;
        this.srv = proj.getPlugin().getServer();
        for (OfflinePlayer op : srv.getOfflinePlayers()) {
            UuidPlayer up = proj.getUuidPlayer(op.getUniqueId());
            offlineMap.put(op.getUniqueId(), up);
        }
    }

    @Override public int size() {
        return offlineMap.size();
    }

    @Override public boolean isEmpty() {
        return offlineMap.isEmpty();
    }

    @Override public boolean contains(Object o) {
        return offlineMap.values().contains(o);
    }

    @NotNull @Override public Iterator<UuidPlayer> iterator() {
        return offlineMap.values().iterator();
    }

    @NotNull @Override public Object[] toArray() {
        return offlineMap.values().toArray();
    }

    @NotNull @Override public <T> T[] toArray(@NotNull T[] a) {
        return offlineMap.values().toArray(a);
    }

    @Override public boolean add(UuidPlayer up) {
        throw new IllegalArgumentException("OnlineObservable is immutable");
    }

    @Override public boolean remove(Object o) {
        throw new IllegalArgumentException("OnlineObservable is immutable");
    }

    @Override public boolean containsAll(@NotNull Collection<?> c) {
        return Arrays.asList(srv.getOfflinePlayers()).containsAll(c);
    }

    @Override public boolean addAll(@NotNull Collection<? extends UuidPlayer> c) {
        throw new IllegalArgumentException("OnlineObservable is immutable");
    }

    @Override public boolean removeAll(@NotNull Collection<?> c) {
        throw new IllegalArgumentException("OnlineObservable is immutable");
    }

    @Override public boolean retainAll(@NotNull Collection<?> c) {
        throw new IllegalArgumentException("OnlineObservable is immutable");
    }

    @Override public void clear() {
        throw new IllegalArgumentException("OnlineObservable is immutable");
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        UuidPlayer up = offlineMap.get(uuid);
        if(up != null) return;
        up = proj.getUuidPlayer(uuid);
        offlineMap.put(uuid, up);
        fireAdd(up);
    }
    
}
