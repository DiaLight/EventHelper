package dialight.teams.filter.team;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class TeamFilterSlot implements Slot {

    @NotNull private final Teams proj;
    private final ObservableTeam oteam;

    public TeamFilterSlot(Teams proj, ObservableTeam oteam) {
        this.proj = proj;
        this.oteam = oteam;
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT:
                Collection<String> filter = proj.getTeamFilter();
                if (filter.contains(oteam.getName())) {
                    filter.remove(oteam.getName());
                } else {
                    filter.add(oteam.getName());
                }
                break;
            case SHIFT_LEFT:
                break;
            case RIGHT:
                break;
            case SHIFT_RIGHT:
                break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        boolean inFilter = proj.getTeamFilter().contains(oteam.getName());
        Material material;
        if (inFilter) {
            material = Material.LEATHER_BOOTS;
        } else {
            material = Material.LEATHER_CHESTPLATE;
        }
        ItemStackBuilder isb = new ItemStackBuilder(material)
                .displayName(oteam.getColor() + Colorizer.apply("⬛ |w|" + oteam.getName()))
                .leatherArmorColor(oteam.getLeatherColor())
                .hideAttributes(true)
                .hideMiscellaneous(true);
        if (inFilter) {
            isb.lore(Colorizer.asList(
                    "|a|ЛКМ|y|: убрать из фильтра"
            ));
        } else {
            isb.lore(Colorizer.asList(
                    "|a|ЛКМ|y|: добавить в фильтр"
            ));
        }
        return isb.build();
    }

}
