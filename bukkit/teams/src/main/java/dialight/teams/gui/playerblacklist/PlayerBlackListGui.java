package dialight.teams.gui.playerblacklist;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.Teams;
import org.bukkit.entity.Player;

public class PlayerBlackListGui extends Gui {

    private final Teams proj;
    private final PlayerBlackListElement layout;
    private final PlayerBlackListView view;

    public PlayerBlackListGui(Teams proj) {
        this.proj = proj;
        this.layout = new PlayerBlackListElement(proj);
        this.view = new PlayerBlackListView(this, layout);
    }

    @Override public View createView(Player player) {
        return view;
    }

    public Teams getProj() {
        return proj;
    }

}
