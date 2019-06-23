package dialight.teams.gui.addteam;

import dialight.guilib.layout.CachedPageLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.view.page.Scroll9x5PageView;
import org.bukkit.ChatColor;

public class AddTeamView extends Scroll9x5PageView<AddTeamGui, CachedPageLayout<ChatColor>> {

    private final Slot background = buildDefaultBackground();
    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);

    public AddTeamView(AddTeamGui gui, CachedPageLayout<ChatColor> layout) {
        super(gui, layout, "Добавление команды");
    }

    @Override public void updateView() {
        defaultUpdateView(this, background, forward, backward);
    }

}
