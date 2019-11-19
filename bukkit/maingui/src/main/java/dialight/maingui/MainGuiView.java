package dialight.maingui;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.elements.HorizontalMultiElement;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.page.Scroll7x6PageView;
import org.bukkit.DyeColor;

public class MainGuiView extends Scroll7x6PageView<MainGui, HorizontalMultiElement> {

    private final Slot toolsBackground = new StaticSlot(new ItemStackBuilder()
            .let(builder -> {
                ItemStackBuilderBc.of(builder).stainedGlassPane(DyeColor.LIGHT_BLUE);
            })
            .displayName(Colorizer.apply("|a|Инструменты"))
            .build());
    private final Slot modulesBackground = new StaticSlot(new ItemStackBuilder()
            .let(builder -> {
                ItemStackBuilderBc.of(builder).stainedGlassPane(DyeColor.GREEN);
            })
            .displayName(Colorizer.apply("|a|Модули"))
            .build());

    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);

    public MainGuiView(MainGui gui, HorizontalMultiElement layout, String title) {
        super(gui, layout, title);
    }

    private Slot createViewBackground(int x, int y) {
        int px = 0;
        if(x == 0) px = getOffset();
        else px = getOffset() + getWidth() - 1;
        int index = getLayout().getLayoutIndex(px);
        if(index == 0) return toolsBackground;
        return modulesBackground;
    }

    @Override protected void updateViewPanels() {
        int limit = calcLimit();
        for (int y = 0; y < 6; y++) {
            if((y == 2 || y == 3) && getOffset() != 0) {
                setLeftPaneSlot(y, backward);
            } else {
                setLeftPaneSlot(y, createViewBackground(0, y));
            }
            if((y == 2 || y == 3) && getOffset() != limit) {
                setRightPaneSlot(y, forward);
            } else {
                setRightPaneSlot(y, createViewBackground(8, y));
            }
        }
    }

}
