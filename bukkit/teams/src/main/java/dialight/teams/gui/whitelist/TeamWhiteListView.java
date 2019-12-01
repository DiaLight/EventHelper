package dialight.teams.gui.whitelist;

import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.page.Scroll9x5PageView;
import org.bukkit.Material;

public class TeamWhiteListView extends Scroll9x5PageView<TeamWhiteListGui, TeamWhiteListElement> {

    private final Slot background = buildDefaultBackground();
    private final Slot forward = buildDefaultForward(this);
    private final Slot backward = buildDefaultBackward(this);

    public TeamWhiteListView(TeamWhiteListGui gui, TeamWhiteListElement layout) {
        super(gui, layout, "Белый список команд");

        Slot clear = new StaticSlot(new ItemStackBuilder(Material.LAVA_BUCKET)
                .displayName(Colorizer.apply("|a|Очистка"))
                .addLore(Colorizer.asList(
                        "|a|Shift|y|+|a|ЛКМ|y|: очистить белый список"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT:
                        break;
                    case SHIFT_LEFT:
                        gui.getProj().getTeamWhiteList().clear();
                        break;
                    case RIGHT:
                        break;
                    case SHIFT_RIGHT:
                        break;
                }
            }
        };
        setBotPaneSlot(8, clear);
    }

    @Override protected Slot createBotBackground(int x) {
        return defaultCreateBotBackground(this, x, background, forward, backward);
    }

}
