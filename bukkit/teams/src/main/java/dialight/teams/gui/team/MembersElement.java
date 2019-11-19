package dialight.teams.gui.team;

import dialight.guilib.elements.NamedElement;
import dialight.guilib.elements.NamedSetElement;
import dialight.guilib.slot.Slot;
import dialight.observable.collection.ObservableCollection;
import dialight.offlinelib.OnlineObservable;
import dialight.offlinelib.UuidPlayer;
import dialight.teams.Teams;
import dialight.teams.observable.ObservableTeam;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MembersElement extends NamedSetElement<UuidPlayer, UUID> {

    private final Teams proj;
    private final ObservableTeam oteam;

    public MembersElement(Teams proj, ObservableTeam oteam) {
        super(5, UuidPlayer::getUuid);
        this.proj = proj;
        this.oteam = oteam;

        setNameFunction(UuidPlayer::getName);
        setSlotFunction(this::buildSlot);
    }

    @Override public void onViewersNotEmpty() {
        ObservableCollection<UuidPlayer> members = oteam.getMembers();
        members.onAdd(this, this::onMemberAdd);
        members.onRemove(this, this::onMemberRemove);
        members.forEach(this::add);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.onAdd(this, this::onAddOnline);
        online.onRemove(this, this::onRemoveOnline);
    }

    @Override public void onViewersEmpty() {
        ObservableCollection<UuidPlayer> members = oteam.getMembers();
        members.removeListeners(this);

        OnlineObservable online = proj.getOfflinelib().getOnline();
        online.removeListeners(this);

        proj.runTask(this::clear);
    }

    private void dumpThrow(UuidPlayer op, NamedElement<UuidPlayer> layout) {
        layout.dump();
        throw new RuntimeException("Somethings wrong");
    }

    private void onMemberAdd(UuidPlayer op) {
        if(!add(op)) dumpThrow(op, this);
    }

    private void onMemberRemove(UuidPlayer op) {
        if(!remove(op)) dumpThrow(op, this);
    }

    private void onAddOnline(Player player) {
        UuidPlayer up = proj.getOfflinelib().getUuidPlayer(player);
        boolean isMember = oteam.getMembers().contains(up);
        if(update(up) != isMember) dumpThrow(up, this);
    }

    private void onRemoveOnline(Player player) {
        UuidPlayer up = proj.getOfflinelib().getUuidPlayer(player);
        boolean isMember = oteam.getMembers().contains(up);
        if(update(up) != isMember) dumpThrow(up, this);
    }

    private Slot buildSlot(UuidPlayer up) {
        return new MemberSlot(this.proj, this.oteam, up);
    }

}
