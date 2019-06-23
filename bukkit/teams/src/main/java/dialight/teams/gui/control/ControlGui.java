package dialight.teams.gui.control;

import dialight.guilib.gui.Gui;
import dialight.guilib.slot.Slot;
import dialight.guilib.view.View;
import dialight.teams.Teams;
import org.bukkit.entity.Player;

public class ControlGui extends Gui {

    private final Teams proj;
    private final ControlLayout layout;
    private final ControlView view;

    public ControlGui(Teams proj) {
        this.proj = proj;
        this.layout = new ControlLayout();
        this.view = new ControlView(this, layout);
    }

    @Override public View createView(Player player) {
        return this.view;
    }

    public void addControlItem(Slot slot) {
        layout.add(slot);
    }

}
