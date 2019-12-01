package dialight.teams.captain.gui.select;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.captain.SortByCaptain;
import org.bukkit.entity.Player;

public class SelectMemberGui extends Gui {

    private final SortByCaptain proj;
    private final SelectMemberLayout layout;

    public SelectMemberGui(SortByCaptain proj) {
        this.proj = proj;
        this.layout = new SelectMemberLayout(proj);
    }

    @Override public View createView(Player player) {
        return new SelectMemberView(this, layout);
    }

    public SortByCaptain getProj() {
        return proj;
    }

}
