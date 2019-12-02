package dialight.teams.gui.results;

import dialight.guilib.elements.CachedPageElement;
import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.slot.Slot;
import dialight.teams.TeamSortResult;
import dialight.teams.Teams;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ResultsElement extends CachedPageElement<TeamSortResult> {

    @NotNull private final Teams proj;

    public ResultsElement(Teams proj) {
        super(new SparkIndexCache(9, 5));
//        super(new SpiralIndexCache(9, 5));
        this.proj = proj;

        this.setNameFunction(TeamSortResult::getName);
        this.setSlotFunction(this::createSlot);
    }

    private Slot createSlot(TeamSortResult result) {
        return new ResultsSlot(proj, result);
    }

    @Override public void onViewersNotEmpty() {
        proj.getSortResult().onChange(this, this::onChange);
        proj.getSortResult().getValue().values().forEach(this::add);
    }

    @Override public void onViewersEmpty() {
        proj.getSortResult().removeListeners(this);
        proj.runTask(this::clear);
    }

    private void onChange(Map<String, ? extends TeamSortResult> oldValue, Map<String, ? extends TeamSortResult> newValue) {
        clear();
        newValue.values().forEach(this::add);
    }

}
