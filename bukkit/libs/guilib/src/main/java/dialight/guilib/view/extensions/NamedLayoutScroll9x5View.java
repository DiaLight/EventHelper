package dialight.guilib.view.extensions;

import dialight.guilib.gui.Gui;
import dialight.guilib.layout.NamedLayout;
import dialight.guilib.layout.SlotLayout;
import dialight.guilib.view.Scroll9x5View;

public abstract class NamedLayoutScroll9x5View<G extends Gui, L extends SlotLayout> extends Scroll9x5View<G, L> {

    public NamedLayoutScroll9x5View(G gui, L layout) {
        super(gui, layout, "");
    }

    @Override protected int calcLimit() {
        int limit = getLayout().getWidth() - width;
        if(limit < 0) limit = 0;
        return limit;
    }

    public abstract NamedLayout getNamedLayout();

    @Override protected void updateTitle() {
        NamedLayout<?> layout = getNamedLayout();
        String header = layout.buildColumnsHeader(offset, width);
        StringBuilder titleBuilder = new StringBuilder();
        for (int i = 0; i < header.length(); i++) {
            char c = header.charAt(i);
            titleBuilder.append("  ").append(c);
            titleBuilder.append((i % 2 == 0) ? " " : "  ");
        }
        setTitle(titleBuilder.toString());
    }

}
