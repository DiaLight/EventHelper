package dialight.teams.gui.team;

import dialight.compatibility.TeamBc;
import dialight.extensions.ColorConverter;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.extensions.OfflinePlayerEx;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.observable.collection.ObservableCollection;
import dialight.offlinelib.OfflineLibApi;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import dialight.teleporter.TeleporterApi;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MemberSlot implements Slot {

    private final Teams proj;
    private final ObservableTeam oteam;
    private final UUID uuid;
    @NotNull private final Server server;
    @NotNull private final Scoreboard scoreboard;

    public MemberSlot(Teams proj, ObservableTeam oteam, UUID uuid) {
        this.proj = proj;
        this.oteam = oteam;
        this.uuid = uuid;
        this.server = proj.getPlugin().getServer();
        this.scoreboard = proj.getPlugin().getServer().getScoreboardManager().getMainScoreboard();
    }

    @Override public void onClick(SlotClickEvent e) {
        OfflinePlayer op = server.getOfflinePlayer(this.uuid);
        ObservableCollection<OfflinePlayer> members = oteam.getMembers();
        switch (e.getEvent().getClick()) {
            case LEFT:
                if(members.contains(op)) {
                    members.remove(op);
                } else {
                    members.add(op);
                }
                break;
            case SHIFT_LEFT:
                break;
            case RIGHT:
                break;
            case SHIFT_RIGHT:
                TeleporterApi teleporter = proj.getTeleporter();
                if (teleporter != null) {
                    OfflineLibApi offlinelib = proj.getOfflinelib();
                    OfflinePlayerEx opex = offlinelib.getOfflinePlayerEx(uuid);
                    if(opex != null) {
                        Location to = opex.getLocation();
                        teleporter.teleport(e.getPlayer(), to);
                    } else {
                        e.getPlayer().sendMessage(Colorizer.apply("|r|Игрок не найден"));
                    }
                }
                break;
        }
    }

    @NotNull @Override public ItemStack createItem() {
        OfflinePlayer op = server.getOfflinePlayer(this.uuid);
        Team team = scoreboard.getEntryTeam(op.getName());
        boolean isOnline = op.isOnline();
        boolean inCurTeam = oteam.getMembers().contains(op);
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
            isb.displayName(op.getName());
        } else {
            isb.displayName(op.getName() + Colorizer.apply(" |r|(Офлайн)"));
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
        isb.addLore(Colorizer.apply("|y|uuid: |w|" + op.getUniqueId()));
        return isb.build();
    }

}
