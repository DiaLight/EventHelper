package dialight.teams.gui.control;

import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.elements.CachedPageElement;
import dialight.guilib.slot.Slot;
import dialight.teams.Teams;

public class ControlElement extends CachedPageElement<Slot> {

    private final Teams proj;

    public ControlElement(Teams proj) {
        super(new SparkIndexCache(9, 5));
        this.proj = proj;
    }

    @Override public void onViewersNotEmpty() {

    }

    @Override public void onViewersEmpty() {

    }
}
