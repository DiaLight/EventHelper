package dialight.teams.gui.playerblacklist;

import dialight.guilib.elements.NamedElement;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.extensions.NamedElementScroll9X5View;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import org.bukkit.Material;

public class PlayerBlackListView extends NamedElementScroll9X5View<PlayerBlackListGui, PlayerBlackListElement> {

    private final Slot background = buildDefaultBackground();
    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);

    public PlayerBlackListView(PlayerBlackListGui gui, PlayerBlackListElement layout) {
        super(gui, layout);
        Slot selectView = new StaticSlot(new ItemStackBuilder(Material.BOOK)
                .displayName("Выбор представления данных")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Все игроки",
                        "|a|ПКМ|y|: Игроки не в черном списке",
                        "|a|Shift|y|+|a|ПКМ|y|: Игроки в черном списке"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        getLayout().setAllLayout();
                    } break;
                    case RIGHT: {
                        getLayout().setNotInBLLayout();
                    } break;
                    case SHIFT_RIGHT: {
                        getLayout().setInBLLayout();
                    } break;
                }
            }
        };
        Slot clear = new StaticSlot(new ItemStackBuilder(Material.LAVA_BUCKET)
                .displayName(Colorizer.apply("|a|Очистка"))
                .addLore(Colorizer.asList(
                        "|a|Shift|y|+|a|ЛКМ|y|: очистить черный список"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT:
                        break;
                    case SHIFT_LEFT:
                        gui.getProj().getPlayerBlackList().clear();
                        break;
                    case RIGHT:
                        break;
                    case SHIFT_RIGHT:
                        break;
                }
            }
        };
        this.setBotPaneSlot(0, selectView);
        this.setBotPaneSlot(8, clear);
        this.setEmptyTitleReplace("Черный список игроков");
    }

    @Override public NamedElement getNamedLayout() {
        return getLayout().getCurrent();
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

}
