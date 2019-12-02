package dialight.teams.gui.results;


import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.misc.player.UuidPlayer;
import dialight.teams.TeamSortResult;
import dialight.teams.Teams;
import dialight.teams.observable.ObservableTeam;
import dialight.teleporter.SelectedPlayers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

public class ResultsSlot implements Slot {

    @NotNull private final Teams proj;
    @NotNull private final TeamSortResult result;
    private final ItemStack item;

    public ResultsSlot(Teams proj, TeamSortResult result) {
        this.proj = proj;
        this.result = result;
        this.item = new ItemStackBuilder(Material.LEATHER_CHESTPLATE)
                .leatherArmorColor(result.getLeatherColor())
                .hideAttributes(true)
                .hideMiscellaneous(true)
                .displayName(result.getColor() + Colorizer.apply("⬛ |w|" + result.getName()))
                .lore(Colorizer.asList(
                        "|a|ЛКМ|y|: внести игроков в телепортер",
                        "|a|ПКМ|y|: Телепортировать игроков в точку вхождения",
                        "|w|У команд должны быть установлены точки вхождения",
                        "|a|Shift|y|+|a|ПКМ|y|: Добавить игроков в соответствующую команду"
                ))
                .build();
    }

    @Override public void onClick(SlotClickEvent e) {
        switch (e.getEvent().getClick()) {
            case LEFT: {
                SelectedPlayers selectedPlayers = proj.getTeleporter().getSelectedPlayers(e.getPlayer().getUniqueId());
                selectedPlayers.clear();
                selectedPlayers.addAllUuidPlayers(result.getMembers());
            } break;
            case RIGHT: {
                Location entryPoint = proj.getTeamEntryPoints().get(result.getName());
                if(entryPoint != null) {
                    for (UuidPlayer member : result.getMembers()) {
                        member.teleport(entryPoint);
                    }
                }
            } break;
            case SHIFT_LEFT: {

            } break;
            case SHIFT_RIGHT: {
                ObservableTeam team = proj.getScoreboardManager().getMainScoreboard().teamsByName().get(result.getName());
                team.getMembers().addAll(result.getMembers());
            } break;
            case MIDDLE: {

            } break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        ItemStack item = this.item.clone();
        List<UuidPlayer> members = result.getMembers();
        item.setAmount(members.size());
        ItemStackBuilder isb = new ItemStackBuilder(item);
        if(members.isEmpty()) {
            isb.addLore(Colorizer.asList(
                    "|g|В команде нет игроков"
            ));
        } else {
            isb.addLore(Colorizer.asList(
                    "|g|Игроки в команде:"
            ));
            Iterator<UuidPlayer> iterator = members.iterator();
            int previewSize = 8;
            for (int i = 0; i < previewSize && iterator.hasNext(); i++) {
                UuidPlayer up = iterator.next();
                String name = up.getName();
                isb.addLore(Colorizer.asList(
                        result.getColor() + "⬛ |w|" + name
                ));
            }
            if (members.size() > previewSize) {
                int left = members.size() - previewSize;
                isb.addLore(Colorizer.asList(
                        "|g|и еще |w|" + left + "|g| игроков"
                ));
            }
        }
        return isb.build();
    }

}
