package dialight.teams.gui.playerblacklist;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.compatibility.TeamBc;
import dialight.extensions.ColorConverter;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.observable.collection.ObservableCollection;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.Teams;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerBlackListSlot implements Slot {

    private final Teams proj;
    private final UuidPlayer up;
    @NotNull private final Scoreboard scoreboard;

    public PlayerBlackListSlot(Teams proj, UuidPlayer up) {
        this.proj = proj;
        this.up = up;
        this.scoreboard = proj.getPlugin().getServer().getScoreboardManager().getMainScoreboard();
    }

    @Override public void onClick(SlotClickEvent e) {
        ObservableCollection<UUID> filter = proj.getPlayerBlackList();
        switch (e.getEvent().getClick()) {
            case LEFT:
                if(filter.contains(up.getUuid())) {
                    filter.remove(up.getUuid());
                } else {
                    filter.add(up.getUuid());
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
        Team team = scoreboard.getEntryTeam(up.getName());
        boolean isOnline = up.isOnline();
        boolean inFilter = proj.getPlayerBlackList().contains(up.getUuid());
        ItemStackBuilder isb = new ItemStackBuilder();
        if(team != null) {
            ItemStackBuilderBc isbbc = ItemStackBuilderBc.of(isb);
            DyeColor dyeColor = ColorConverter.toWoolColor(TeamBc.of(team).getColor());
            if(isOnline) {
                if (inFilter) {
                    isbbc.wool(dyeColor);
                } else {
                    isbbc.stainedGlass(dyeColor);
                }
            } else {
                if (inFilter) {
                    isbbc.stainedGlassPane(dyeColor);
                } else {
                    isbbc.carpet(dyeColor);
                }
            }
        } else {
            if(inFilter) {
                if(isOnline) {
                    isb.reset(Material.DIAMOND);
                } else {
                    isb.reset(Material.DIAMOND_ORE);
                }
            } else {
                if(isOnline) {
                    isb.reset(Material.COAL);
                } else {
                    isb.reset(Material.COAL_ORE);
                }
            }
        }
        if (isOnline) {
            isb.displayName(Colorizer.apply("|w|" + up.getName()));
        } else {
            isb.displayName(Colorizer.apply("|w|" + up.getName() + " |r|(Офлайн)"));
        }
        if (inFilter) {
            isb.addLore(Colorizer.apply("|a|ЛКМ|y|: убрать игрока из черного списка"));
        } else {
            isb.addLore(Colorizer.apply("|a|ЛКМ|y|: добавить игрока в черный список"));
        }
        if(team != null) {
            isb.addLore(Colorizer.apply("|y|team: " + TeamBc.of(team).getColor() + "⬛ |w|" + team.getName()));
        }
        isb.addLore(Colorizer.apply("|y|uuid: |w|" + up.getUuid()));
        return isb.build();
    }

}
