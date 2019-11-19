package dialight.teams.gui;

import dialight.guilib.elements.FixedElement;
import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.Teams;
import org.bukkit.entity.Player;

public class TeamsLobbyGui extends Gui {

    private final Teams proj;
//    private final SingleElementLayout layout = new SingleElementLayout(new FixedElement(9, 6));
    private final FixedElement layout = new FixedElement(9, 6);
    private final TeamsLobbyView view;

    public TeamsLobbyGui(Teams proj) {
        this.proj = proj;
        this.view = new TeamsLobbyView(this, this.layout);
    }

    @Override public View createView(Player player) {
        return view;
    }

}
