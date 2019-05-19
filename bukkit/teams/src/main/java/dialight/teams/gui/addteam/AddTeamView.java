package dialight.teams.gui.addteam;

import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.view.Scroll9x5View;
import org.bukkit.ChatColor;

public class AddTeamView extends Scroll9x5View<AddTeamGui, CachedPageLayout<ChatColor>> {

    public AddTeamView(AddTeamGui gui, CachedPageLayout<ChatColor> layout) {
        super(gui, layout, "Добавление команды");

    }

}
