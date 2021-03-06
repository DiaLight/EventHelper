package dialight.teams.gui.sort;

import dialight.guilib.slot.Slot;
import dialight.guilib.view.page.Scroll9x5PageView;
import dialight.teams.gui.teams.PlayerBlackListSlot;

public class SortView extends Scroll9x5PageView<SortGui, SortElement> {

    private final Slot background = buildDefaultBackground();
    private final Slot forward = buildDefaultForward(this);
    private final Slot backward = buildDefaultBackward(this);

    public SortView(SortGui gui, SortElement layout) {
        super(gui, layout, "Сортировщики команд");
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

}
