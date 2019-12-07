package dialight.teams.captain.gui.captain.member;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.captain.SortByCaptain;
import org.bukkit.entity.Player;

public class SelectCaptainGui extends Gui {

    private final SortByCaptain proj;
    private final SelectCaptainLayout layout;

    public SelectCaptainGui(SortByCaptain proj) {
        this.proj = proj;
        this.layout = new SelectCaptainLayout(proj);
    }

    @Override public View createView(Player player) {
        return new SelectCaptainView(this, layout);
    }

    public SortByCaptain getProj() {
        return proj;
    }

}
