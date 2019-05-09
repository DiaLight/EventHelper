package dialight.maingui;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.Colorizer;
import dialight.extensions.GuiUtils;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.layout.HorizontalMultiLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.Scroll7x6View;
import org.bukkit.DyeColor;

public class MainGuiView extends Scroll7x6View<MainGui, HorizontalMultiLayout> {

    private final Slot toolsBackground = new StaticSlot(new ItemStackBuilder()
            .let(builder -> {
                ItemStackBuilderBc.of(builder).stainedGlassPane(DyeColor.LIGHT_BLUE);
            })
            .displayName("Инструменты")
            .build());
    private final Slot modulesBackground = new StaticSlot(new ItemStackBuilder()
            .let(builder -> {
                ItemStackBuilderBc.of(builder).stainedGlassPane(DyeColor.GREEN);
            })
            .displayName("Модули")
            .build());

    private final Slot backward = new StaticSlot(new ItemStackBuilder()
            .let(builder -> {
                ItemStackBuilderBc.of(builder).playerHead();
            })
            .displayName("Инструменты")
            .lore(Colorizer.asList(
                    "|a|ЛКМ|y|: Перейти на предыдущую страницу"
            ))
            .nbt(GuiUtils.BACKWARD_NBT)
            .build()) {
        @Override
        public void onClick(SlotClickEvent e) {
            switch (e.getEvent().getClick()) {
                case LEFT: {
//                        moveBackward(1);
                    moveBackward(width);
                } break;
                case SHIFT_LEFT: {
//                        moveBackward(width);
                } break;
            }
        }
    };

    private final Slot forward = new StaticSlot(new ItemStackBuilder()
            .let(builder -> {
                ItemStackBuilderBc.of(builder).playerHead();
            })
            .displayName("Инструменты")
            .lore(Colorizer.asList(
                    "|a|ЛКМ|y|: Перейти на следующую страницу"
            ))
            .nbt(GuiUtils.FORWARD_NBT)
            .build()) {
        @Override
        public void onClick(SlotClickEvent e) {
            switch (e.getEvent().getClick()) {
                case LEFT: {
//                        moveForward(1);
                    moveForward(width);
                } break;
                case SHIFT_LEFT: {
//                        moveForward(width);
                }
            }
        }
    };

    public MainGuiView(MainGui gui, HorizontalMultiLayout layout, String title) {
        super(gui, layout, title);
    }

    @Override protected int calcLimit() {
        int limit = getLayout().getWidth() - width;
        if(limit < 0) limit = 0;
        return limit;
    }

    private Slot createViewBackground(int x, int y) {
        int px = 0;
        if(x == 0) px = offset;
        else px = offset + width - 1;
        int index = getLayout().getLayoutIndex(px);
        if(index == 0) return toolsBackground;
        return modulesBackground;
    }

    @Override protected void updateViewPanels() {
        int limit = calcLimit();
        for (int y = 0; y < 6; y++) {
            if((y == 2 || y == 3) && offset != 0) {
                setLeftPaneSlot(y, backward);
            } else {
                setLeftPaneSlot(y, createViewBackground(0, y));
            }
            if((y == 2 || y == 3) && offset != limit) {
                setRightPaneSlot(y, forward);
            } else {
                setRightPaneSlot(y, createViewBackground(8, y));
            }
        }
    }

    @Override protected void updateTitle() {
        int pages = (getLayout().getWidth() + width - 1) / width;
        int page = (offset + width - 1) / width;
        setTitle(prefix + " " + (page + 1) + "/" + pages);
    }

}
