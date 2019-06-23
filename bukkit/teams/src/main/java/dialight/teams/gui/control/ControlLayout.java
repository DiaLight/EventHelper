package dialight.teams.gui.control;

import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;

public class ControlLayout extends CachedPageLayout<Slot> {

    public ControlLayout() {
        super(new SparkIndexCache(9, 5));
    }

}
