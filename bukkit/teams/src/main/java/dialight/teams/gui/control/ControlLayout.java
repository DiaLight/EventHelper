package dialight.teams.gui.control;

import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;
import dialight.teams.Teams;

public class ControlLayout extends CachedPageLayout<Slot> {

    private final Teams proj;

    public ControlLayout(Teams proj) {
        super(new SparkIndexCache(9, 5));
        this.proj = proj;
    }

    @Override public void onViewersNotEmpty() {
        proj.getListener().registerTeamsObserver(this);
    }

    @Override public void onViewersEmpty() {
        proj.getListener().unregisterTeamsObserver(this);
    }
}
