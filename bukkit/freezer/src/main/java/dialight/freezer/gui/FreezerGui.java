package dialight.freezer.gui;

import dialight.freezer.Freezer;
import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import org.bukkit.entity.Player;

public class FreezerGui extends Gui {

    private final Freezer proj;
    private final FreezerViewState element;

    public FreezerGui(Freezer proj) {
        this.proj = proj;
        this.element = new FreezerViewState(proj);
    }

    @Override
    public View createView(Player player) {
        return new FreezerView(this, element);
    }

    public Freezer getFreezer() {
        return proj;
    }

}
