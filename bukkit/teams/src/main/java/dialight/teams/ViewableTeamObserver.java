package dialight.teams;

import dialight.guilib.Viewable;
import dialight.observable.collection.ObservableCollection;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class ViewableTeamObserver implements Function<Player, Player> {

    private final Viewable viewable;

    public ViewableTeamObserver(Viewable viewable) {
        this.viewable = viewable;
    }

    @Override public Player apply(Player player) {
        ObservableCollection<Player> viewers = viewable.getViewers();
        if(player == null) {
            if(viewers.isEmpty()) return null;
            return viewers.iterator().next();
        }
        for (Player viewer : viewers) {
            if(viewer.getUniqueId() == player.getUniqueId()) continue;
            return viewer;
        }
        return null;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ViewableTeamObserver that = (ViewableTeamObserver) o;

        return viewable.equals(that.viewable);
    }

    @Override public int hashCode() {
        return viewable.hashCode();
    }

}
