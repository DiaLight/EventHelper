package dialight.teams.gui.teams;

import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.teams.Teams;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class ResultsSlot extends StaticSlot {

    private final Teams proj;

    public ResultsSlot(Teams proj) {
        super(createItem_());
        this.proj = proj;
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT: {
                proj.getGuilib().openGui(e.getPlayer(), proj.getResultsGui());
            } break;
            case SHIFT_LEFT:
                break;
            case RIGHT: {

            } break;
            case SHIFT_RIGHT:
                break;
        }
    }

    private static ItemStack createItem_() {
        ItemStackBuilder isb = new ItemStackBuilder(Material.FISHING_ROD);
        isb.displayName(Colorizer.apply("|a|Результат последней сортировки"));
        isb.addLore(Colorizer.asList(
                "|a|ЛКМ|y|: Открыть гуи"
        ));
        isb.hideMiscellaneous(true);
        return isb.build();
    }

}

