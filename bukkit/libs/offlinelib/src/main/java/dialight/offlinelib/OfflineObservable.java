package dialight.offlinelib;

import dialight.observable.collection.ObservableCollection;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class OfflineObservable extends ObservableCollection<OfflinePlayer> implements Listener {

    private final Server srv;
    private final Map<UUID, OfflinePlayer> offlineMap = new HashMap<>();
    private final OfflineLib proj;

    public OfflineObservable(OfflineLib proj) {
        this.proj = proj;
        this.srv = proj.getPlugin().getServer();
        for (OfflinePlayer op : srv.getOfflinePlayers()) {
            offlineMap.put(op.getUniqueId(), op);
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

    @NotNull @Override public Iterator<OfflinePlayer> iterator() {
        return offlineMap.values().iterator();
    }

    @NotNull @Override public Object[] toArray() {
        return offlineMap.values().toArray();
    }

    @NotNull @Override public <T> T[] toArray(@NotNull T[] a) {
        return offlineMap.values().toArray(a);
    }

    @Override public boolean add(OfflinePlayer OfflinePlayer) {
        throw new IllegalArgumentException("OnlineObservable is immutable");
    }

    @Override public boolean remove(Object o) {
        throw new IllegalArgumentException("OnlineObservable is immutable");
    }

    @Override public boolean containsAll(@NotNull Collection<?> c) {
        return Arrays.asList(srv.getOfflinePlayers()).containsAll(c);
    }

    @Override public boolean addAll(@NotNull Collection<? extends OfflinePlayer> c) {
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
        OfflinePlayer op = offlineMap.get(uuid);
        if(op != null) return;
        op = srv.getOfflinePlayer(uuid);
        offlineMap.put(uuid, op);
        fireAdd(op);
    }
    
}
