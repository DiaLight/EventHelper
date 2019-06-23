package dialight.teams.random.gui.filter;

import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;
import dialight.observable.collection.ObservableCollection;
import dialight.teams.ObservableTeam;
import dialight.teams.random.TeamRandomizerProject;

import java.util.function.Consumer;

public class FilterLayout extends CachedPageLayout<ObservableTeam> {

    private final TeamRandomizerProject proj;
    private final Consumer<ObservableTeam> onAdd = this::add;
    private final Consumer<ObservableTeam> onRemove = this::remove;
    private final Consumer<String> onFilterAdd = this::onFilterAdd;
    private final Consumer<String> onFilterRemove = this::onFilterRemove;

    public FilterLayout(TeamRandomizerProject proj) {
        super(new SparkIndexCache(9, 5));
        this.proj = proj;
        this.setNameFunction(ObservableTeam::getName);
        this.setSlotFunction(this::createSlot);
    }

    @Override public void onViewersNotEmpty() {
        ObservableCollection<ObservableTeam> teams = proj.getTeams().getTeams();
        teams.onAdd(onAdd);
        teams.onRemove(onRemove);

        ObservableCollection<String> filter = proj.getFilter();
        filter.onAdd(onFilterAdd);
        filter.onRemove(onFilterRemove);

        teams.forEach(this::add);
    }

    @Override public void onViewersEmpty() {
        ObservableCollection<ObservableTeam> teams = proj.getTeams().getTeams();
        teams.unregisterOnAdd(onAdd);
        teams.unregisterOnRemove(onRemove);

        ObservableCollection<String> filter = proj.getFilter();
        filter.unregisterOnAdd(onFilterAdd);
        filter.unregisterOnRemove(onFilterRemove);

        proj.runTask(this::clear);
    }

    private void onFilterAdd(String name) {
        ObservableTeam oteam = proj.getTeams().get(name);
        if(oteam != null) {
            update(oteam);
        }
    }

    private void onFilterRemove(String name) {
        ObservableTeam oteam = proj.getTeams().get(name);
        if(oteam != null) {
            update(oteam);
        }
    }

    private Slot createSlot(ObservableTeam oteam) {
        return new FilterSlot(proj, oteam);
    }

}
