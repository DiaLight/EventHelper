package dialight.teams.gui.teams;

import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;
import dialight.observable.collection.ObservableCollection;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TeamsLayout extends CachedPageLayout<ObservableTeam> {

    @NotNull private final Teams proj;
    private final Consumer<ObservableTeam> onAdd = this::onAdd;
    private final Consumer<ObservableTeam> onRemove = this::onRemove;
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
//        slot.ti = size() + 1;
        return slot;
    }

    @Override public void onViewersNotEmpty() {
        proj.getListener().registerTeamsObserver(this);

        proj.onTeamUpdate(onTeamUpdate);

        ObservableCollection<ObservableTeam> teams = proj.getTeamsInternal();
        teams.onAdd(onAdd);
        teams.onRemove(onRemove);
        teams.forEach(this::add);
    }

    @Override public void onViewersEmpty() {
        proj.getListener().unregisterTeamsObserver(this);

        proj.unregisterOnTeamUpdate(onTeamUpdate);

        ObservableCollection<ObservableTeam> teams = proj.getTeamsInternal();
        teams.unregisterOnAdd(onAdd);
        teams.unregisterOnRemove(onRemove);
        proj.runTask(this::clear);
    }

    private void onAdd(ObservableTeam oteam) {
        this.add(oteam);
    }
    private void onRemove(ObservableTeam oteam) {
        this.remove(oteam);
    }

}
