package dialight.teams.captain.gui.captain.member;

import dialight.guilib.elements.NamedElement;
import dialight.guilib.slot.Slot;
import dialight.guilib.view.extensions.NamedElementScroll9X5View;

public class SelectCaptainView extends NamedElementScroll9X5View<SelectCaptainGui, SelectCaptainLayout> {

    private final Slot background = buildDefaultBackground();
    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);

    public SelectCaptainView(SelectCaptainGui gui, SelectCaptainLayout layout) {
        super(gui, layout);
    }

    @Override public NamedElement getNamedLayout() {
        return getLayout();
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

}
