package dialight.teams.gui.teams;

import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.DynamicSlot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.observable.collection.ObservableCollection;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class TeamWhiteListSlot extends DynamicSlot {

    private final Teams proj;

    public TeamWhiteListSlot(Teams proj) {
        this.proj = proj;
        ObservableCollection<String> teamFilter = proj.getTeamWhiteList();
        teamFilter.onAdd(this::update);
        teamFilter.onRemove(this::update);
    }

    private void update(String name) {
        updateLater(proj.getPlugin());
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT: {
                proj.getGuilib().openGui(e.getPlayer(), proj.getTeamWhiteListGui());
            } break;
            case RIGHT: {
            } break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        ItemStackBuilder isb = new ItemStackBuilder(Material.ENCHANTED_BOOK)
                .displayName(Colorizer.apply("|a|Белый список команд"))
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: открыть белый список команд"
                        ));
        isb.hideMiscellaneous(true);
        if(proj.getTeamWhiteList().isEmpty()) {
            isb.addLore(Colorizer.asList(
                    "|g|Белый список команд пуст"
            ));
        } else {
            isb.addLore(Colorizer.asList(
                    "|g|Команды в белом списке:"
            ));
            for (String name : proj.getTeamWhiteList()) {
                ObservableTeam team = proj.get(name);
                if(team != null) {
                    isb.addLore(Colorizer.asList(
                            "|g|- " + team.getColor() + Colorizer.apply("⬛ |w|" + name)
                    ));
                } else {
                    isb.addLore(Colorizer.asList(
                            "|g|- |w|" + name
                    ));
                }
            }
        }
        return isb.build();
    }

}
