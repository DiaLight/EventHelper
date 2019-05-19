package dialight.teams.gui.team;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.entity.Player;

public class TeamGui implements Gui {


    private final Teams proj;
    private final ObservableTeam oteam;
    private final TeamLayout layout;
    private final TeamView view;

    public TeamGui(Teams proj, ObservableTeam oteam) {
        this.proj = proj;
        this.oteam = oteam;
        this.layout = new TeamLayout(proj, oteam);
        this.view = new TeamView(this, layout);
    }

    @Override
    public View createView(Player player) {
        return view;
    }

    public void unregister() {
        for (Player viewer : this.view.getViewers()) {
            viewer.closeInventory();
        }
        this.layout.unregister();
    }

    public ObservableTeam getOteam() {
        return oteam;
    }

    public Teams getProj() {
        return proj;
    }

}
