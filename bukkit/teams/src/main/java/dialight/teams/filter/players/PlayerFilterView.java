package dialight.teams.filter.players;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.layout.NamedLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.extensions.NamedLayoutScroll9x5View;
import org.bukkit.Material;

public class PlayerFilterView extends NamedLayoutScroll9x5View<PlayerFilterGui, PlayerFilterLayout> {

    private final Slot background = buildDefaultBackground();
    private final Slot backward = buildDefaultBackward(this);
    private final Slot forward = buildDefaultForward(this);
    private final Slot selectView;
    private final Slot config;


    public PlayerFilterView(PlayerFilterGui gui, PlayerFilterLayout layout) {
        super(gui, layout);
        selectView = new StaticSlot(new ItemStackBuilder(Material.BOOK)
                .displayName("Выбор представления данных")
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: Все игроки",
                        "|a|ПКМ|y|: Игроки не в фильтре",
                        "|a|Shift|y|+|a|ПКМ|y|: Игроки в фильтре"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT: {
                        getLayout().setAllLayout();
                    } break;
                    case RIGHT: {
                        getLayout().setNotInFilterLayout();
                    } break;
                    case SHIFT_RIGHT: {
                        getLayout().setInFilterLayout();
                    } break;
                }
            }
        };
        config = new PlayerFilterConfigSlot(gui.getProj());
        this.setBotPaneSlot(0, selectView);
        this.setBotPaneSlot(1, config);
    }

    @Override public NamedLayout getNamedLayout() {
        return getLayout().getCurrent();
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

}
