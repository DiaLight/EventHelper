package dialight.offlinelib;

import dialight.observable.collection.ObservableCollection;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Iterator;

public class OnlineObservable extends ObservableCollection<Player> implements Listener {

    private final Server srv;
    private final OfflineLib proj;

    public OnlineObservable(OfflineLib proj) {
        this.proj = proj;
        this.srv = proj.getPlugin().getServer();
    }

    @Override public int size() {
        return srv.getOnlinePlayers().size();
    }

    @Override
    public boolean isEmpty() {
        return srv.getOnlinePlayers().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return srv.getOnlinePlayers().contains(o);
    }

    @NotNull
    @Override
    public Iterator<Player> iterator() {
        return (Iterator<Player>) srv.getOnlinePlayers().iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return srv.getOnlinePlayers().toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return srv.getOnlinePlayers().toArray(a);
    }

    @Override
    public boolean add(Player player) {
        throw new IllegalArgumentException("OnlineObservable is immutable");
    }

    @Override
    public boolean remove(Object o) {
        throw new IllegalArgumentException("OnlineObservable is immutable");
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return srv.getOnlinePlayers().containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Player> c) {
        throw new IllegalArgumentException("OnlineObservable is immutable");
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        throw new IllegalArgumentException("OnlineObservable is immutable");
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new IllegalArgumentException("OnlineObservable is immutable");
    }

    @Override
    public void clear() {
        throw new IllegalArgumentException("OnlineObservable is immutable");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        fireAdd(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        proj.runTask(() -> fireRemove(player));
    }

}
