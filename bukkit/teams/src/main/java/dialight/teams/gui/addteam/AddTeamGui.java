package dialight.teams.gui.addteam;

import dialight.guilib.gui.Gui;
import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.view.View;
import dialight.teams.Teams;
import org.bukkit.entity.Player;

public class AddTeamGui extends Gui {

    private final Teams proj;
    private final AddTeamLayout layout;
    private final AddTeamView view;

    public AddTeamGui(Teams proj) {
        this.proj = proj;
        this.layout = new AddTeamLayout(proj, new SparkIndexCache(9, 5));
        this.view = new AddTeamView(this, layout);
    }

    @Override public View createView(Player player) {
        return view;
    }

    public Teams getProj() {
        return proj;
    }

}
