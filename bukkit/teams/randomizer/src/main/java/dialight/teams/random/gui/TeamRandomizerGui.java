package dialight.teams.random.gui;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.random.TeamRandomizerProject;
import org.bukkit.entity.Player;

public class TeamRandomizerGui extends Gui {

    private final TeamRandomizerProject proj;
    private final TeamRandomizerElement layout;
    private final TeamRandomizerView view;

    public TeamRandomizerGui(TeamRandomizerProject proj) {
        this.proj = proj;
        this.layout = new TeamRandomizerElement(proj);
        this.view = new TeamRandomizerView(this, this.layout);
    }

    @Override
    public View createView(Player player) {
        return view;
    }

}
