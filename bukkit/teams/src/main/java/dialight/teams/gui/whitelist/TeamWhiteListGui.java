package dialight.teams.gui.whitelist;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.Teams;
import org.bukkit.entity.Player;

public class TeamWhiteListGui extends Gui {

    private final Teams proj;
    private final TeamWhiteListLayout layout;
    private final TeamWhiteListView view;

    public TeamWhiteListGui(Teams proj) {
        this.proj = proj;
        this.layout = new TeamWhiteListLayout(proj);
        this.view = new TeamWhiteListView(this, this.layout);
    }

    @Override
    public View createView(Player player) {
        return this.view;
    }

    public Teams getProj() {
        return proj;
    }
}
