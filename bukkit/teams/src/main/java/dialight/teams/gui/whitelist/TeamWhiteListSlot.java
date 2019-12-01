package dialight.teams.gui.whitelist;

import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.observable.set.ObservableSet;
import dialight.teams.Teams;
import dialight.teams.observable.ObservableScoreboard;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TeamWhiteListSlot implements Slot {

    @NotNull private final Teams proj;
    private final ObservableScoreboard scoreboard;
    private final String name;

    public TeamWhiteListSlot(Teams proj, ObservableScoreboard scoreboard, String name) {
        this.proj = proj;
        this.scoreboard = scoreboard;
        this.name = name;
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT:
                ObservableSet<String> filter = proj.getTeamWhiteList();
                if (filter.contains(name)) {
                    filter.remove(name);
                } else {
                    filter.add(name);
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
        boolean inFilter = proj.getTeamWhiteList().contains(name);
        ObservableTeam oteam = scoreboard.teamsByName().get(name);
        ItemStackBuilder isb = new ItemStackBuilder();
        if(oteam != null) {
            if (inFilter) {
                isb.reset(Material.LEATHER_BOOTS);
            } else {
                isb.reset(Material.LEATHER_CHESTPLATE);
            }
            isb.displayName(oteam.color().getValue() + Colorizer.apply("⬛ |w|" + oteam.getName()));
            isb.leatherArmorColor(oteam.getLeatherColor());
        } else {
            isb.reset(Material.ARMOR_STAND);
            isb.displayName(Colorizer.apply("|w|" + name));
        }
        isb.hideAttributes(true);
        isb.hideMiscellaneous(true);
        if (inFilter) {
            isb.lore(Colorizer.asList(
                    "|a|ЛКМ|y|: убрать из белого списка"
            ));
        } else {
            isb.lore(Colorizer.asList(
                    "|a|ЛКМ|y|: добавить в белый список"
            ));
        }
        return isb.build();
    }

}
