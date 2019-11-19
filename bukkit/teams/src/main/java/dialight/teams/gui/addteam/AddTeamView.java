package dialight.teams.gui.addteam;

import dialight.guilib.elements.CachedPageElement;
import dialight.guilib.slot.Slot;
import dialight.guilib.view.page.Scroll9x5PageView;
import org.bukkit.ChatColor;

public class AddTeamView extends Scroll9x5PageView<AddTeamGui, CachedPageElement<ChatColor>> {

    private final Slot background = buildDefaultBackground();
    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);

    public AddTeamView(AddTeamGui gui, CachedPageElement<ChatColor> layout) {
        super(gui, layout, "Добавление команды");
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

}
