package dialight.teams.captain.gui.captain.team;

import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.guilib.view.page.Scroll9x5PageView;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.teams.captain.SortByCaptain;
import org.bukkit.Material;

public class CaptainTeamView extends Scroll9x5PageView<CaptainTeamGui, CaptainTeamElement> {

    private final Slot background = buildDefaultBackground();
    private final Slot forward = buildDefaultForward(this);
    private final Slot backward = buildDefaultBackward(this);

    public CaptainTeamView(CaptainTeamGui gui, CaptainTeamElement layout) {
        super(gui, layout, "Выбор капитанов для команд");
        SortByCaptain proj = gui.getProj();
        Slot clear = new StaticSlot(new ItemStackBuilder(Material.LAVA_BUCKET)
                .displayName(Colorizer.apply("|a|Рандом"))
                .addLore(Colorizer.asList(
                        "|a|ЛКМ|y|: Рандомно выбирать капитанов"
                ))
                .build()) {
            @Override
            public void onClick(SlotClickEvent e) {
                switch (e.getEvent().getClick()) {
                    case LEFT:
                        proj.getCaptainSelection().clear(e.getPlayer());
                        break;
                    case SHIFT_LEFT:
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
