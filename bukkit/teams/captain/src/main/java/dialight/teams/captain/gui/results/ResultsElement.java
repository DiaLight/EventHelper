package dialight.teams.captain.gui.results;

import dialight.guilib.elements.CachedPageElement;
import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.slot.Slot;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.captain.TeamSortResult;
import org.jetbrains.annotations.NotNull;

public class ResultsElement extends CachedPageElement<TeamSortResult> {

    @NotNull private final SortByCaptain proj;

    public ResultsElement(SortByCaptain proj) {
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
        proj.getSortResult().onPut(this, this::onAdd);
        proj.getSortResult().onRemove(this, this::onRemove);

        proj.getSortResult().forEach(this::onAdd);
    }

    @Override public void onViewersEmpty() {
        proj.getSortResult().removeListeners(this);

        proj.runTask(this::clear);
    }

    private void onAdd(String name, TeamSortResult result) {
        this.add(result);
    }
    private void onRemove(String name, TeamSortResult result) {
        this.remove(result);
    }

}
