package dialight.teams.captain.gui.control;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.captain.SortByCaptain;
import org.bukkit.entity.Player;

public class ControlGui extends Gui {

    private final SortByCaptain proj;
    private final ControlLayout layout;

    public ControlGui(SortByCaptain proj) {
        this.proj = proj;
        this.layout = new ControlLayout(proj);
    }

    @Override public View createView(Player player) {
        return new ControlView(this, layout);
    }

    public SortByCaptain getProj() {
        return proj;
    }

}
