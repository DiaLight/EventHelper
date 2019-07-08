package dialight.teams.gui.team;

import dialight.guilib.layout.NamedLayout;
import dialight.guilib.layout.NamedSetLayout;
import dialight.guilib.slot.Slot;
import dialight.observable.collection.ObservableCollection;
import dialight.offlinelib.OfflineObservable;
import dialight.offlinelib.OnlineObservable;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

public class NotMembersLayout extends NamedSetLayout<UuidPlayer, UUID> {

    private final Teams proj;
    private final ObservableTeam oteam;
    private final Consumer<UuidPlayer> onMemberAdd = this::onMemberAdd;
    private final Consumer<UuidPlayer> onMemberRemove = this::onMemberRemove;
    private final Consumer<Player> onAddOnline = this::onAddOnline;
    private final Consumer<Player> onRemoveOnline = this::onRemoveOnline;

    public NotMembersLayout(Teams proj, ObservableTeam oteam) {
        super(5, UuidPlayer::getUuid);
        this.proj = proj;
        this.oteam = oteam;

        setNameFunction(UuidPlayer::getName);
        setSlotFunction(this::buildSlot);
    }

    @Override public void onViewersNotEmpty() {
        ObservableCollection<UuidPlayer> members = oteam.getMembers();
        members.onAdd(onMemberAdd);
        members.onRemove(onMemberRemove);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.onAdd(onAddOnline);
        online.onRemove(onRemoveOnline);

        OfflineObservable offline = proj.getOfflinelib().getOffline();
        for (OfflinePlayer op : offline) {
            UuidPlayer up = proj.getOfflinelib().getUuidPlayer(op.getUniqueId());
            if (!oteam.getMembers().contains(up)) {
                add(up);
            }
        }
        for (UuidPlayer unp : proj.getOfflinelib().getNotPlayers()) {
            if (!oteam.getMembers().contains(unp)) {
                add(unp);
            }
        }
    }

    @Override public void onViewersEmpty() {
        ObservableCollection<UuidPlayer> members = oteam.getMembers();
        members.unregisterOnAdd(onMemberAdd);
        members.unregisterOnRemove(onMemberRemove);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.unregisterOnAdd(onAddOnline);
        online.unregisterOnRemove(onRemoveOnline);

        proj.runTask(this::clear);
    }

    private void dumpThrow(UuidPlayer up, NamedLayout<UuidPlayer> layout) {
        layout.dump();
        throw new RuntimeException("Somethings wrong. " + up.getName() + " " + up.getUuid());
    }

    private void onMemberAdd(UuidPlayer up) {
        if(!remove(up)) dumpThrow(up, this);
    }

    private void onMemberRemove(UuidPlayer up) {
        if(!add(up)) dumpThrow(up, this);
    }

    private void onAddOnline(Player player) {
        UuidPlayer up = proj.getOfflinelib().getUuidPlayer(player);
        boolean isMember = oteam.getMembers().contains(up);
        update(up);
    }

    private void onRemoveOnline(Player player) {
        UuidPlayer up = proj.getOfflinelib().getUuidPlayer(player);
        boolean isMember = oteam.getMembers().contains(up);
        update(up);
    }

    private Slot buildSlot(UuidPlayer up) {
        return new MemberSlot(this.proj, this.oteam, up);
    }

}
