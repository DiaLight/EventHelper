package dialight.teams.gui.team;

import dialight.compatibility.TeamBc;
import dialight.extensions.ColorConverter;
import dialight.extensions.Colorizer;
import dialight.extensions.ItemStackBuilder;
import dialight.guilib.layout.NamedLayout;
import dialight.guilib.layout.NamedSetLayout;
import dialight.guilib.layout.ReplaceableLayout;
import dialight.guilib.slot.Slot;
import dialight.guilib.slot.SlotClickEvent;
import dialight.observable.collection.ObservableCollection;
import dialight.offlinelib.OfflineLibApi;
import dialight.offlinelib.OfflineObservable;
import dialight.offlinelib.OnlineObservable;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import dialight.teleporter.TeleporterApi;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class TeamLayout extends ReplaceableLayout<NamedLayout<OfflinePlayer>> {

    private class MemberSlot implements Slot {

        private final UUID uuid;

        public MemberSlot(UUID uuid) {
            this.uuid = uuid;
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
                        Location to = offlinelib.getOfflinePlayerEx(uuid).getLocation();
                        teleporter.teleport(e.getPlayer(), to);
                    }
                    break;
            }
        }

        @NotNull
        @Override public ItemStack createItem() {
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
            return isb.build();
        }

    }

    private final NamedLayout<OfflinePlayer> teamLayout = new NamedSetLayout<>(5, OfflinePlayer::getUniqueId);
    private final NamedLayout<OfflinePlayer> notTeamLayout = new NamedSetLayout<>(5, OfflinePlayer::getUniqueId);
    private final NamedLayout<OfflinePlayer> notCurTeamLayout = new NamedSetLayout<>(5, OfflinePlayer::getUniqueId);
    @NotNull private final Teams proj;
    @NotNull private final Server server;
    @NotNull private final Scoreboard scoreboard;
    @NotNull private final ObservableTeam oteam;

    public TeamLayout(Teams proj, ObservableTeam oteam) {
        this.proj = proj;
        this.server = proj.getPlugin().getServer();
        this.scoreboard = proj.getPlugin().getServer().getScoreboardManager().getMainScoreboard();
        this.oteam = oteam;

        teamLayout.setNameFunction(OfflinePlayer::getName);
        teamLayout.setSlotFunction(this::buildSlot);
        notTeamLayout.setNameFunction(OfflinePlayer::getName);
        notTeamLayout.setSlotFunction(this::buildSlot);
        notCurTeamLayout.setNameFunction(OfflinePlayer::getName);
        notCurTeamLayout.setSlotFunction(this::buildSlot);

        ObservableCollection<OfflinePlayer> members = oteam.getMembers();
        members.onAdd(this::onMemberAdd);
        members.onRemove(this::onMemberRemove);
        for (OfflinePlayer op : members) {
            teamLayout.add(op);
        }

        ObservableCollection<OfflinePlayer> notInTeam = proj.getNotInTeamImmutable();
        notInTeam.onAdd(this::onNotInTeamAdd);
        notInTeam.onRemove(this::onNotInTeamRemove);
        for (OfflinePlayer op : notInTeam) {
            notTeamLayout.add(op);
        }

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.onAdd(this::onAddOnline);
        online.onRemove(this::onRemoveOnline);

        OfflineObservable offline = proj.getOfflinelib().getOffline();
        for (OfflinePlayer op : offline) {
            if (!oteam.getMembers().contains(op)) {
                notCurTeamLayout.add(op);
            }
        }

        setCurrent(teamLayout);
    }

    public void unregister() {
        ObservableCollection<OfflinePlayer> members = oteam.getMembers();
        members.unregisterOnAdd(this::onMemberAdd);
        members.unregisterOnRemove(this::onMemberRemove);

        ObservableCollection<OfflinePlayer> notInTeam = proj.getNotInTeamImmutable();
        notInTeam.unregisterOnAdd(this::onNotInTeamAdd);
        notInTeam.unregisterOnRemove(this::onNotInTeamRemove);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.unregisterOnAdd(this::onAddOnline);
        online.unregisterOnRemove(this::onRemoveOnline);
    }

    private void dumpThrow(OfflinePlayer op, NamedLayout<OfflinePlayer> layout) {
        layout.dump();
        throw new RuntimeException("Somethings wrong");
    }

    private void onMemberAdd(OfflinePlayer op) {
        if(!teamLayout.add(op)) dumpThrow(op, teamLayout);
        if(!notCurTeamLayout.remove(op)) dumpThrow(op, notCurTeamLayout);
    }

    private void onMemberRemove(OfflinePlayer op) {
        if(!teamLayout.remove(op)) dumpThrow(op, teamLayout);
        if(!notCurTeamLayout.add(op)) dumpThrow(op, notCurTeamLayout);
    }

    private void onNotInTeamAdd(OfflinePlayer op) {
//        System.out.println("onNotInTeamAdd " + op);
        if(!notTeamLayout.add(op)) dumpThrow(op, notTeamLayout);
    }

    private void onNotInTeamRemove(OfflinePlayer op) {
//        System.out.println("onNotInTeamRemove " + op);
        if(!notTeamLayout.remove(op)) dumpThrow(op, notTeamLayout);
    }

    private void onAddOnline(Player player) {
        OfflinePlayer op = player;
        ObservableCollection<OfflinePlayer> members = oteam.getMembers();
        if(teamLayout.update(op) != members.contains(op)) dumpThrow(op, teamLayout);
        notTeamLayout.update(op);
        if(notCurTeamLayout.update(op) == members.contains(op)) dumpThrow(op, notCurTeamLayout);
    }

    private void onRemoveOnline(Player player) {
        OfflinePlayer op = player;
        if(teamLayout.update(op) != oteam.getMembers().contains(op)) dumpThrow(op, teamLayout);
        notTeamLayout.update(op);
        if(notCurTeamLayout.update(op) == oteam.getMembers().contains(op)) dumpThrow(op, notCurTeamLayout);
    }

    private Slot buildSlot(OfflinePlayer op) {
        return new MemberSlot(op.getUniqueId());
    }

    public NamedLayout<OfflinePlayer> getTeamLayout() {
        return teamLayout;
    }

    public NamedLayout<OfflinePlayer> getNotTeamLayout() {
        return notTeamLayout;
    }

    public NamedLayout<OfflinePlayer> getNotCurTeamLayout() {
        return notCurTeamLayout;
    }

}
