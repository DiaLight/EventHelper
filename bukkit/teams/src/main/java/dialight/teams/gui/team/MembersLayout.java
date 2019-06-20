package dialight.teams.gui.team;

import dialight.guilib.layout.NamedLayout;
import dialight.guilib.layout.NamedSetLayout;
import dialight.guilib.slot.Slot;
import dialight.observable.collection.ObservableCollection;
import dialight.offlinelib.OnlineObservable;
import dialight.teams.ObservableTeam;
import dialight.teams.Teams;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.function.Consumer;

public class MembersLayout extends NamedSetLayout<OfflinePlayer, UUID> {

    private final Teams proj;
    private final ObservableTeam oteam;
    private final Consumer<OfflinePlayer> onMemberAdd = this::onMemberAdd;
    private final Consumer<OfflinePlayer> onMemberRemove = this::onMemberRemove;
    private final Consumer<Player> onAddOnline = this::onAddOnline;
    private final Consumer<Player> onRemoveOnline = this::onRemoveOnline;

    public MembersLayout(Teams proj, ObservableTeam oteam) {
        super(5, OfflinePlayer::getUniqueId);
        this.proj = proj;
        this.oteam = oteam;

        setNameFunction(OfflinePlayer::getName);
        setSlotFunction(this::buildSlot);
    }

    @Override public void onViewersNotEmpty() {
        proj.update();

        ObservableCollection<OfflinePlayer> members = oteam.getMembers();
        members.onAdd(onMemberAdd);
        members.onRemove(onMemberRemove);
        for (OfflinePlayer op : members) {
            add(op);
        }

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.onAdd(onAddOnline);
        online.onRemove(onRemoveOnline);
    }

    @Override public void onViewersEmpty() {
        ObservableCollection<OfflinePlayer> members = oteam.getMembers();
        members.unregisterOnAdd(onMemberAdd);
        members.unregisterOnRemove(onMemberRemove);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.unregisterOnAdd(onAddOnline);
        online.unregisterOnRemove(onRemoveOnline);

        proj.runTask(this::clear);
    }

    private void dumpThrow(OfflinePlayer op, NamedLayout<OfflinePlayer> layout) {
        layout.dump();
        throw new RuntimeException("Somethings wrong");
    }

    private void onMemberAdd(OfflinePlayer op) {
        if(!add(op)) dumpThrow(op, this);
    }

    private void onMemberRemove(OfflinePlayer op) {
        if(!remove(op)) dumpThrow(op, this);
    }

    private void onAddOnline(Player player) {
        OfflinePlayer op = player;
        boolean isMember = oteam.getMembers().contains(op);
        if(update(op) != isMember) dumpThrow(op, this);
    }

    private void onRemoveOnline(Player player) {
        OfflinePlayer op = player;
        boolean isMember = oteam.getMembers().contains(op);
        if(update(op) != isMember) dumpThrow(op, this);
    }

    private Slot buildSlot(OfflinePlayer op) {
        return new MemberSlot(this.proj, this.oteam, op.getUniqueId());
    }

}
