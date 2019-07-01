package dialight.guilib.view.page;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.Colorizer;
import dialight.extensions.GuiUtils;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.gui.Gui;
import dialight.guilib.layout.SlotLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.Scroll7x6View;

public abstract class Scroll7x6PageView<G extends Gui, L extends SlotLayout> extends Scroll7x6View<G, L> {

    public Scroll7x6PageView(G gui, L layout, String title) {
        super(gui, layout, title);
    }

    @Override protected int calcLimit() {
        int limit = getLayout().getWidth() - getWidth();
        if(limit < 0) limit = 0;
        return limit;
    }

    @Override protected void updateTitle() {
        int width = getWidth();
        int page = (getOffset() / width) + 1;
        int pages = ((getLayout().getWidth() + width - 1) / width);
        if(pages <= 1) {
            setTitle(prefix);
        } else {
            setTitle(prefix + " " + page + "/" + pages);
        }
    }

    private static Slot buildDefaultForward(Scroll7x6View view) {
        return new StaticSlot(new ItemStackBuilder()
                .let(builder -> {
                    ItemStackBuilderBc.of(builder).playerHead();
                })
                .displayName("Вперед")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Перейти на предыдущую страницу"
                ))
                .nbt(GuiUtils.FORWARD_NBT)
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        view.moveForward(view.getWidth());
                    } break;
                    case SHIFT_LEFT: {
                    } break;
                }
            }
        };
    }
    private static Slot buildDefaultBackward(Scroll7x6View view) {
        return new StaticSlot(new ItemStackBuilder()
                .let(builder -> {
                    ItemStackBuilderBc.of(builder).playerHead();
                })
                .displayName("Назад")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Перейти на предыдущую страницу"
                ))
                .nbt(GuiUtils.BACKWARD_NBT)
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        view.moveBackward(view.getWidth());
                    } break;
                    case SHIFT_LEFT: {
                    } break;
                }
            }
        };
    }

}
