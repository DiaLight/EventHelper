package dialight.guilib;

import dialight.observable.collection.ObservableCollection;
import dialight.observable.collection.ObservableCollectionWrapper;
import org.bukkit.entity.Player;

import java.util.HashSet;

public abstract class Viewable {

    private final ObservableCollection<Player> viewers = new ObservableCollectionWrapper<>(new HashSet<>());
    private final ObservableCollection<Player> immutableViewers = viewers.asImmutable();

    public final ObservableCollection<Player> getViewers() {
        return immutableViewers;
    }
    public final void fireOpenView(Player player) {
        boolean isEmpty = viewers.isEmpty();
        viewers.add(player);
        onOpenView(player);
        if(isEmpty) onViewersNotEmpty();
    }
    public final void fireCloseView(Player player) {
        viewers.remove(player);
        onCloseView(player);
        if(viewers.isEmpty()) onViewersEmpty();
    }

    public void onOpenView(Player player) {}
    public void onCloseView(Player player) {}

    public void onViewersNotEmpty() {}
    public void onViewersEmpty() {}

}
