package dialight.teams.captain.gui.results;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.captain.SortByCaptain;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ResultsGui extends Gui {

    @NotNull private final SortByCaptain proj;
    private final ResultsView view;
    private final ResultsElement layout;

    public ResultsGui(SortByCaptain proj) {
        this.proj = proj;
        this.layout = new ResultsElement(proj);
        this.view = new ResultsView(this, layout);
    }

    @Override public View createView(Player player) {
        return view;
    }

    @NotNull public SortByCaptain getProj() {
        return proj;
    }


}
