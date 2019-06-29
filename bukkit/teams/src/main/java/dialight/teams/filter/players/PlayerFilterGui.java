package dialight.teams.filter.players;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.Teams;
import org.bukkit.entity.Player;

public class PlayerFilterGui extends Gui {

    private final Teams proj;
    private final PlayerFilterLayout layout;
    private final PlayerFilterView view;

    public PlayerFilterGui(Teams proj) {
        this.proj = proj;
        this.layout = new PlayerFilterLayout(proj);
        this.view = new PlayerFilterView(this, layout);
    }

    @Override public View createView(Player player) {
        return view;
    }

    public Teams getProj() {
        return proj;
    }

}
