package dialight.teams.gui.control;

import dialight.guilib.slot.Slot;
import dialight.guilib.view.page.Scroll9x5PageView;

public class ControlView extends Scroll9x5PageView<ControlGui, ControlLayout> {

    private final Slot background = buildDefaultBackground();
    private final Slot forward = buildDefaultForward(this);
    private final Slot backward = buildDefaultBackward(this);

    public ControlView(ControlGui gui, ControlLayout layout) {
        super(gui, layout, "Распределители команд");
    }

    @Override protected void updateView() {
        defaultUpdateView(this, background, forward, backward);
    }

}
