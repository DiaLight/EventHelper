package dialight.teams.gui.playerblacklist;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.layout.NamedLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.extensions.NamedLayoutScroll9x5View;
import org.bukkit.Material;

public class PlayerBlackListView extends NamedLayoutScroll9x5View<PlayerBlackListGui, PlayerBlackListLayout> {

    private final Slot background = buildDefaultBackground();
    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);
    private final Slot selectView;
    private final Slot config;


    public PlayerBlackListView(PlayerBlackListGui gui, PlayerBlackListLayout layout) {
        super(gui, layout);
        selectView = new StaticSlot(new ItemStackBuilder(Material.BOOK)
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
        config = new PlayerBlackListConfigSlot(gui.getProj());
        this.setBotPaneSlot(0, selectView);
        this.setBotPaneSlot(1, config);
        this.setBotPaneSlot(8, clear);
        this.setEmptyTitleReplace("Черный список игроков");
    }

    @Override public NamedLayout getNamedLayout() {
        return getLayout().getCurrent();
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

}
