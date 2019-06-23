package dialight.teams.random.gui.filter;

import dialight.guilib.slot.Slot;
import dialight.guilib.view.page.Scroll9x5PageView;

public class FilterView extends Scroll9x5PageView<FilterGui, FilterLayout> {

    private final Slot background = buildDefaultBackground();
    private final Slot forward = buildDefaultForward(this);
    private final Slot backward = buildDefaultBackward(this);

    public FilterView(FilterGui gui, FilterLayout layout, String title) {
        super(gui, layout, title);
    }

    @Override protected void updateView() {
        defaultUpdateView(this, background, forward, backward);
    }

}
