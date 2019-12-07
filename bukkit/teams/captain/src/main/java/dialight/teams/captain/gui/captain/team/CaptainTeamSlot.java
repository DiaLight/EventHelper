package dialight.teams.captain.gui.captain.team;

import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.teams.captain.SortByCaptain;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CaptainTeamSlot implements Slot {

    @NotNull private final SortByCaptain proj;
    private final ObservableTeam oteam;

    public CaptainTeamSlot(SortByCaptain proj, ObservableTeam oteam) {
        this.proj = proj;
        this.oteam = oteam;
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT:
                proj.getCaptainSelection().selectTeam(e.getPlayer(), this.oteam);
                break;
            case SHIFT_LEFT:
                proj.getCaptainSelection().clear(e.getPlayer(), this.oteam);
                break;
            case RIGHT:
                break;
            case SHIFT_RIGHT:
                break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        ItemStackBuilder isb = new ItemStackBuilder();
        isb.reset(Material.LEATHER_CHESTPLATE);
        isb.displayName(oteam.color().getValue() + Colorizer.apply("⬛ |w|" + oteam.getName()));
        isb.leatherArmorColor(oteam.getLeatherColor());
        isb.hideAttributes(true);
        isb.hideMiscellaneous(true);
        isb.lore(Colorizer.asList(
                "|a|ЛКМ|y|: выбрать капитана для этой команды",
                "|a|Shift|y|+|a|ЛКМ|y|: очистить выбор(рандом)"
        ));
        return isb.build();
    }

}
