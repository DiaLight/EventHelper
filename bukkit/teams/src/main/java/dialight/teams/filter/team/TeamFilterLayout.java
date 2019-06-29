package dialight.teams.filter.team;

import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;
import dialight.observable.collection.ObservableCollection;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;

import java.util.function.Consumer;

public class TeamFilterLayout extends CachedPageLayout<ObservableTeam> {

    private final Teams proj;
    private final Consumer<ObservableTeam> onAdd = this::add;
    private final Consumer<ObservableTeam> onRemove = this::remove;
    private final Consumer<String> onFilterAdd = this::onFilterAdd;
    private final Consumer<String> onFilterRemove = this::onFilterRemove;

    public TeamFilterLayout(Teams proj) {
        super(new SparkIndexCache(9, 5));
        this.proj = proj;
        this.setNameFunction(ObservableTeam::getName);
        this.setSlotFunction(this::createSlot);
    }

    @Override public void onViewersNotEmpty() {
        ObservableCollection<ObservableTeam> teams = proj.getTeamsImmutable();
        teams.onAdd(onAdd);
        teams.onRemove(onRemove);

        ObservableCollection<String> filter = proj.getTeamFilter();
        filter.onAdd(onFilterAdd);
        filter.onRemove(onFilterRemove);

        teams.forEach(this::add);
    }

    @Override public void onViewersEmpty() {
        ObservableCollection<ObservableTeam> teams = proj.getTeamsImmutable();
        teams.unregisterOnAdd(onAdd);
        teams.unregisterOnRemove(onRemove);

        ObservableCollection<String> filter = proj.getTeamFilter();
        filter.unregisterOnAdd(onFilterAdd);
        filter.unregisterOnRemove(onFilterRemove);

        proj.runTask(this::clear);
    }

    private void onFilterAdd(String name) {
        ObservableTeam oteam = proj.get(name);
        if(oteam != null) {
            update(oteam);
        }
    }

    private void onFilterRemove(String name) {
        ObservableTeam oteam = proj.get(name);
        if(oteam != null) {
            update(oteam);
        }
    }

    private Slot createSlot(ObservableTeam oteam) {
        return new TeamFilterSlot(proj, oteam);
    }

}
