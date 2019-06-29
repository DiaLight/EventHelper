package dialight.teams.filter.players;

import dialight.compatibility.ItemStackBuilderBc;
import dialight.compatibility.TeamBc;
import dialight.extensions.ColorConverter;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.observable.collection.ObservableCollection;
import dialight.teams.Teams;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerFilterSlot implements Slot {

    private final Teams proj;
    private final UUID uuid;
    @NotNull private final Server server;
    @NotNull private final Scoreboard scoreboard;

    public PlayerFilterSlot(Teams proj, UUID uuid) {
        this.proj = proj;
        this.uuid = uuid;
        this.server = proj.getPlugin().getServer();
        this.scoreboard = proj.getPlugin().getServer().getScoreboardManager().getMainScoreboard();
    }

    @Override public void onClick(SlotClickEvent e) {
        ObservableCollection<UUID> filter = proj.getPlayerFilter();
        switch (e.getEvent().getClick()) {
            case LEFT:
                if(filter.contains(uuid)) {
                    filter.remove(uuid);
                } else {
                    filter.add(uuid);
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
        OfflinePlayer op = server.getOfflinePlayer(this.uuid);
        Team team = scoreboard.getEntryTeam(op.getName());
        boolean isOnline = op.isOnline();
        boolean inFilter = proj.getPlayerFilter().contains(this.uuid);
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
            isb.displayName(Colorizer.apply("|w|" + op.getName()));
        } else {
            isb.displayName(Colorizer.apply("|w|" + op.getName() + " |r|(Офлайн)"));
        }
        if (inFilter) {
            isb.addLore(Colorizer.apply("|a|ЛКМ|y|: убрать игрока из фильтра"));
        } else {
            isb.addLore(Colorizer.apply("|a|ЛКМ|y|: добавить игрока в фильтр"));
        }
        if(team != null) {
            isb.addLore(Colorizer.apply("|y|team: " + TeamBc.of(team).getColor() + "⬛ |w|" + team.getName()));
        }
        isb.addLore(Colorizer.apply("|y|uuid: |w|" + op.getUniqueId()));
        return isb.build();
    }

}
