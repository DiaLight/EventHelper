package dialight.teams.filter.team;

import dialight.guilib.slot.Slot;
import dialight.guilib.view.page.Scroll9x5PageView;

public class TeamFilterView extends Scroll9x5PageView<TeamFilterGui, TeamFilterLayout> {

    private final Slot background = buildDefaultBackground();
    private final Slot forward = buildDefaultForward(this);
    private final Slot backward = buildDefaultBackward(this);

    public TeamFilterView(TeamFilterGui gui, TeamFilterLayout layout, String title) {
        super(gui, layout, title);
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

}
