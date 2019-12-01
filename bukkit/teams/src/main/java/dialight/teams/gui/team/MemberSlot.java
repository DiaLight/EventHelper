package dialight.teams.gui.team;

import dialight.compatibility.TeamBc;
import dialight.misc.ColorConverter;
import dialight.misc.Colorizer;
import dialight.misc.ItemStackBuilder;
import dialight.extensions.PlayerEx;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.observable.collection.ObservableCollection;
import dialight.misc.player.UuidPlayer;
import dialight.teams.Teams;
import dialight.teams.observable.ObservableTeam;
import dialight.teleporter.TeleporterApi;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public class MemberSlot implements Slot {

    private final Teams proj;
    private final ObservableTeam oteam;
    private final UuidPlayer up;
    @NotNull private final Scoreboard scoreboard;

    public MemberSlot(Teams proj, ObservableTeam oteam, UuidPlayer up) {
        this.proj = proj;
        this.oteam = oteam;
        this.up = up;
        this.scoreboard = proj.getPlugin().getServer().getScoreboardManager().getMainScoreboard();
    }

    @Override public void onClick(SlotClickEvent e) {
        ObservableCollection<UuidPlayer> members = oteam.getMembers();
        switch (e.getEvent().getClick()) {
            case LEFT:
                if(members.contains(up)) {
                    members.remove(up);
                } else {
                    members.add(up);
                }
                break;
            case SHIFT_LEFT:
                break;
            case RIGHT:
                break;
            case SHIFT_RIGHT:
                TeleporterApi teleporter = proj.getTeleporter();
                if (teleporter != null) {
                    PlayerEx.of(e.getPlayer()).teleport(up.getLocation());
                }
                break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        Team team = scoreboard.getEntryTeam(up.getName());
        boolean isOnline = up.isOnline();
        boolean inCurTeam = oteam.getMembers().contains(up);
        Material material;
        if(team != null) {
            if(inCurTeam) {
                if (isOnline) {
                    material = Material.LEATHER_HELMET;
                } else {
                    material = Material.LEATHER_BOOTS;
                }
            } else {
                if (isOnline) {
                    material = Material.LEATHER_CHESTPLATE;
                } else {
                    material = Material.LEATHER_BOOTS;
                }
            }
        } else {
            if(isOnline) {
                material = Material.COAL;
            } else {
                material = Material.COAL_ORE;
            }
        }
        ItemStackBuilder isb = new ItemStackBuilder(material);
        if(team != null) {
            isb.leatherArmorColor(ColorConverter.toLeatherColor(TeamBc.of(team).getColor()));
        }
        if (isOnline) {
            isb.displayName(up.getName());
        } else {
            isb.displayName(up.getName() + Colorizer.apply(" |r|(Офлайн)"));
        }
        if (inCurTeam) {
            isb.addLore(Colorizer.apply("|a|ЛКМ|y|: убрать игрока из текущей команды"));
        } else {
            isb.addLore(Colorizer.apply("|a|ЛКМ|y|: добавить игрока в команду " + oteam.getName()));
        }
//            "~|g|ПКМ|y|: Телепортировать игрока к другому игроку";
        TeleporterApi teleporter = proj.getTeleporter();
        if (teleporter != null) {
            isb.addLore(Colorizer.apply("|a|Shift|y|+|a|ПКМ|y|: телепортироваться к игроку"));
        }
        if(team != null) {
            isb.addLore(Colorizer.apply("|y|team: ") + TeamBc.of(team).getColor() + Colorizer.apply("⬛ |w|" + team.getName()));
            isb.addLore(Colorizer.apply("|y|display name: |w|" + team.getDisplayName()));
        }
        isb.addLore(Colorizer.apply("|y|uuid: |w|" + up.getUuid()));
        return isb.build();
    }

}
