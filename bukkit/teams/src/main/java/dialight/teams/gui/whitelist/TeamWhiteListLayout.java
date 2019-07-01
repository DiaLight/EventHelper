package dialight.teams.gui.whitelist;

import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;
import dialight.observable.collection.ObservableCollection;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;

import java.util.function.Consumer;

public class TeamWhiteListLayout extends CachedPageLayout<String> {

    private final Teams proj;
    private final Consumer<ObservableTeam> onAdd = this::onTeamAdd;
    private final Consumer<ObservableTeam> onRemove = this::onTeamRemove;
    private final Consumer<String> onFilterAdd = this::onFilterAdd;
    private final Consumer<String> onFilterRemove = this::onFilterRemove;

    public TeamWhiteListLayout(Teams proj) {
        super(new SparkIndexCache(9, 5));
        this.proj = proj;
        this.setSlotFunction(this::createSlot);
    }

    @Override public void onViewersNotEmpty() {
        ObservableCollection<ObservableTeam> teams = proj.getTeamsImmutable();
        teams.onAdd(onAdd);
        teams.onRemove(onRemove);

        ObservableCollection<String> filter = proj.getTeamWhiteList();
        filter.onAdd(onFilterAdd);
        filter.onRemove(onFilterRemove);

        teams.forEach(this::onTeamAdd);
        filter.forEach(this::onFilterAdd);
    }

    @Override public void onViewersEmpty() {
        ObservableCollection<ObservableTeam> teams = proj.getTeamsImmutable();
        teams.unregisterOnAdd(onAdd);
        teams.unregisterOnRemove(onRemove);

        ObservableCollection<String> filter = proj.getTeamWhiteList();
        filter.unregisterOnAdd(onFilterAdd);
        filter.unregisterOnRemove(onFilterRemove);

        proj.runTask(this::clear);
    }

    private void onTeamAdd(ObservableTeam oteam) {
        add(oteam.getName());
    }
    private void onTeamRemove(ObservableTeam oteam) {
        update(oteam.getName());
    }

    private void onFilterAdd(String name) {
        ObservableTeam oteam = proj.get(name);
        if(oteam != null) {
            update(name);
        } else {
            add(name);
        }
    }
    private void onFilterRemove(String name) {
        ObservableTeam oteam = proj.get(name);
        if(oteam != null) {
            update(name);
        } else {
            remove(name);
        }
    }

    private Slot createSlot(String name) {
        return new TeamWhiteListSlot(proj, name);
    }

}
