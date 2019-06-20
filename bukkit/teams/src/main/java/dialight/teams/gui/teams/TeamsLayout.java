package dialight.teams.gui.teams;

import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;
import dialight.observable.collection.ObservableCollection;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public class TeamsLayout extends CachedPageLayout<ObservableTeam> {

    @NotNull private final Teams proj;
    private final Consumer<ObservableTeam> onAdd = this::onAdd;
    private final Consumer<ObservableTeam> onRemove = this::onRemove;
    private final Function<Player, Player> getNextPlayer = this::getNextPlayer;
    private final Consumer<ObservableTeam> onTeamUpdate = this::update;

    public TeamsLayout(Teams proj) {
        super(new SparkIndexCache(9, 5));
//        super(new SpiralIndexCache(9, 5));
        this.proj = proj;

        this.setNameFunction(ObservableTeam::getName);
        this.setSlotFunction(this::createSlot);
    }

    private Slot createSlot(ObservableTeam oteam) {
        TeamSlot slot = new TeamSlot(proj, oteam);
        slot.ti = size() + 1;
        return slot;
    }


    @Override public void onViewersNotEmpty() {
        proj.getListener().registerTeamsObserver(getNextPlayer);

        proj.onTeamUpdate(onTeamUpdate);

        ObservableCollection<ObservableTeam> teams = proj.getTeamsInternal();
        teams.onAdd(onAdd);
        teams.onRemove(onRemove);
        teams.forEach(this::add);
    }

    @Override public void onViewersEmpty() {
        proj.getListener().unregisterTeamsObserver(getNextPlayer);

        proj.unregisterOnTeamUpdate(onTeamUpdate);

        ObservableCollection<ObservableTeam> teams = proj.getTeamsInternal();
        teams.unregisterOnAdd(onAdd);
        teams.unregisterOnRemove(onRemove);
        proj.runTask(this::clear);
    }

    private Player getNextPlayer(Player player) {
        ObservableCollection<Player> viewers = getViewers();
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

    private void onAdd(ObservableTeam oteam) {
        this.add(oteam);
    }
    private void onRemove(ObservableTeam oteam) {
        this.remove(oteam);
    }

}
