package dialight.teams.gui.sort;

import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.elements.CachedPageElement;
import dialight.guilib.slot.Slot;
import dialight.teams.Teams;

import java.util.function.Function;

public class SortElement extends CachedPageElement<Slot> {

    private final Teams proj;

    public SortElement(Teams proj) {
        super(new SparkIndexCache(9, 5));
        this.proj = proj;
        setSlotFunction(Function.identity());
    }

    @Override public void onViewersNotEmpty() {

    }

    @Override public void onViewersEmpty() {

    }
}
