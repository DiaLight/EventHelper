package dialight.teams.gui.addteam;

import dialight.guilib.gui.Gui;
import dialight.guilib.indexcache.SparkIndexCache;
import dialight.guilib.view.View;
import dialight.teams.Teams;
import dialight.teams.observable.ObservableScoreboard;
import org.bukkit.entity.Player;

public class AddTeamGui extends Gui {

    private final Teams proj;
    private final AddTeamElement layout;
    private final AddTeamView view;

    public AddTeamGui(Teams proj) {
        this.proj = proj;
        ObservableScoreboard scoreboard = proj.getScoreboardManager().getMainScoreboard();
        this.layout = new AddTeamElement(proj, scoreboard, new SparkIndexCache(9, 5));
        this.view = new AddTeamView(this, layout);
    }

    @Override public View createView(Player player) {
        return view;
    }

    public Teams getProj() {
        return proj;
    }

}
