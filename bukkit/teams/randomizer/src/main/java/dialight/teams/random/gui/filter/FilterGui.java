package dialight.teams.random.gui.filter;

import dialight.guilib.gui.Gui;
import dialight.guilib.view.View;
import dialight.teams.random.TeamRandomizerProject;
import org.bukkit.entity.Player;

public class FilterGui extends Gui {

    private final FilterLayout layout;
    private final FilterView view;

    public FilterGui(TeamRandomizerProject proj) {
        this.layout = new FilterLayout(proj);
        this.view = new FilterView(this, this.layout, "Фильтр команд для рандомизатора");
    }

    @Override
    public View createView(Player player) {
        return this.view;
    }

}
