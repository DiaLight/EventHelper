package dialight.teams.captain.gui.captain.team;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.observable.ObservableScoreboard;
import org.bukkit.entity.Player;

public class CaptainTeamGui extends Gui {

    private final SortByCaptain proj;
    private final CaptainTeamElement layout;
    private final CaptainTeamView view;

    public CaptainTeamGui(SortByCaptain proj, ObservableScoreboard scoreboard) {
        this.proj = proj;
        this.layout = new CaptainTeamElement(proj, scoreboard);
        this.view = new CaptainTeamView(this, this.layout);
    }

    @Override
    public View createView(Player player) {
        return this.view;
    }

    public SortByCaptain getProj() {
        return proj;
    }
}
