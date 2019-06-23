package dialight.guilib.view;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.extensions.Colorizer;
import dialight.extensions.GuiUtils;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.gui.Gui;
import dialight.guilib.layout.SlotLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public abstract class ScrollView<G extends Gui, L extends SlotLayout> extends View<G, L> {

    protected static final List<String> DEFAULT_BACKGROUND_LORE = Colorizer.asList(
            "|r|Для верного отображения",
            "|r|заголовков столбцов",
            "|r|используйте шрифт Unicode.",
            "|w|Навигация",
            "|a|ЛКМ снаружи инвертаря|y|:",
            "|y| Скролл влево",
            "|a|ПКМ снаружи инвертаря|y|:",
            "|y| Скролл вправо",
            "|a|СКМ снаружи инвертаря|y|:",
            "|y| Вернуться назад"
    );

    public ScrollView(G gui, L layout, int width, int height, String title) {
        super(gui, layout, width, height, title);
    }

    @Override public void onOutsideClick(Player player, ClickType click) {
        switch (click) {
            case LEFT: {
                moveBackward(1);
            } break;
            case SHIFT_LEFT: {
                moveBackward(getWidth());
            }
            case RIGHT: {
                moveForward(1);
            } break;
            case SHIFT_RIGHT: {
                moveForward(getWidth());
            } break;
        }
    }

    public abstract void moveBackward(int dx);
    public abstract void moveForward(int dx);

    public abstract int getWidth();
    public abstract int getHeight();
    public abstract int getOffset();

    public static Slot buildDefaultBackground() {
        return new StaticSlot(new ItemStackBuilder()
                .let(builder -> {
                    ItemStackBuilderBc.of(builder).stainedGlassPane(DyeColor.LIGHT_BLUE);
                })
                .displayName(" ")
                .lore(DEFAULT_BACKGROUND_LORE)
                .build());
    }

    public static Slot buildDefaultBackward(ScrollView view) {
        return new StaticSlot(new ItemStackBuilder()
                .let(builder -> {
                    ItemStackBuilderBc.of(builder).playerHead();
                })
                .displayName("Влево")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Скролл влево",
                        "|a|Shift|y|+|a|ЛКМ|y|: Перейти на предыдущую страницу"
                ))
                .nbt(GuiUtils.BACKWARD_NBT)
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        view.moveBackward(1);
                    } break;
                    case SHIFT_LEFT: {
                        view.moveBackward(view.getWidth());
                    } break;
                }
            }
        };
    }

    public static Slot buildDefaultForward(ScrollView view) {
        return new StaticSlot(new ItemStackBuilder()
                .let(builder -> {
                    ItemStackBuilderBc.of(builder).playerHead();
                })
                .displayName("Вправо")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Скролл вправо",
                        "|a|Shift|y|+|a|ЛКМ|y|: Перейти на следующую страницу"
                ))
                .nbt(GuiUtils.FORWARD_NBT)
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        view.moveForward(1);
                    } break;
                    case SHIFT_LEFT: {
                        view.moveForward(view.getWidth());
                    }
                }
            }
        };
    }

}
