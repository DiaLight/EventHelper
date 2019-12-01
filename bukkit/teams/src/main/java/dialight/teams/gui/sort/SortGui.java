package dialight.teams.gui.sort;

import dialight.guilib.gui.Gui;
import dialight.guilib.slot.Slot;
import dialight.guilib.view.View;
import dialight.teams.Teams;
import org.bukkit.entity.Player;

public class SortGui extends Gui {

    private final Teams proj;
    private final SortElement layout;
    private final SortView view;

    public SortGui(Teams proj) {
        this.proj = proj;
        this.layout = new SortElement(proj);
        this.view = new SortView(this, layout);
    }

    @Override public View createView(Player player) {
        return this.view;
    }

    public void addControlItem(Slot slot) {
        layout.add(slot);
    }

    public Teams getProj() {
        return proj;
    }

}
