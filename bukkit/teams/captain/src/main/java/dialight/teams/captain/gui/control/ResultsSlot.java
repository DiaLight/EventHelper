package dialight.teams.captain.gui.control;

import dialight.guilib.slot.SlotClickEvent;
import dialight.guilib.slot.StaticSlot;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.teams.captain.SortByCaptain;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public class ResultsSlot extends StaticSlot {

    private final SortByCaptain proj;

    public ResultsSlot(SortByCaptain proj) {
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
        isb.displayName(Colorizer.apply("|a|Результат сортировки"));
        isb.addLore(Colorizer.asList(
                "|a|ЛКМ|y|: Открыть гуи"
        ));
        isb.hideMiscellaneous(true);
        return isb.build();
    }

}

