package dialight.teams.gui.results;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.Teams;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ResultsGui extends Gui {

    @NotNull private final Teams proj;
    private final ResultsView view;
    private final ResultsElement layout;

    public ResultsGui(Teams proj) {
        this.proj = proj;
        this.layout = new ResultsElement(proj);
        this.view = new ResultsView(this, layout);
    }

    @Override public View createView(Player player) {
        return view;
    }

    @NotNull public Teams getProj() {
        return proj;
    }


}
