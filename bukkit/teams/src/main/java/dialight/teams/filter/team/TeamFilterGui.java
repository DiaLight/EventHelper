package dialight.teams.filter.team;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.Teams;
import org.bukkit.entity.Player;

public class TeamFilterGui extends Gui {

    private final TeamFilterLayout layout;
    private final TeamFilterView view;

    public TeamFilterGui(Teams proj) {
        this.layout = new TeamFilterLayout(proj);
        this.view = new TeamFilterView(this, this.layout, "Фильтр команд для рандомизатора");
    }

    @Override
    public View createView(Player player) {
        return this.view;
    }

}
