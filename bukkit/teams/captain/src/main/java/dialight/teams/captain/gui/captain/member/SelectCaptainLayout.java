package dialight.teams.captain.gui.captain.member;

import dialight.freezer.gui.FreezerViewState;
import dialight.guilib.elements.NamedSetElement;
import dialight.guilib.slot.Slot;
import dialight.misc.player.UuidPlayer;
import dialight.offlinelib.OnlineObservable;
import dialight.teams.captain.SortByCaptain;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SelectCaptainLayout extends NamedSetElement<UuidPlayer, UUID> {

    private final SortByCaptain proj;

    public SelectCaptainLayout(SortByCaptain proj) {
        super(6, UuidPlayer::getUuid);
        this.proj = proj;
        this.setNameFunction(UuidPlayer::getName);
        this.setSlotFunction(this::createSlot);
    }

    @Override public void onViewersNotEmpty() {
        OnlineObservable online = proj.getOfflineLib().getOnline();
        online.onAdd(this, this::onAddOnline);
        online.onRemove(this, this::onRemoveOnline);
        online.forEach(this::onAddOnline);
    }

    @Override public void onViewersEmpty() {
        OnlineObservable online = proj.getOfflineLib().getOnline();
        online.removeListeners(this);

        proj.runTask(this::clear);
    }

    private void onAddOnline(Player player) {
        UuidPlayer up = proj.getOfflineLib().getUuidPlayer(player);
        if(!add(up)) FreezerViewState.dumpThrow(up, this);
    }
    private void onRemoveOnline(Player player) {
        UuidPlayer up = proj.getOfflineLib().getUuidPlayer(player);
        if(!remove(up)) FreezerViewState.dumpThrow(up, this);
    }


    private Slot createSlot(UuidPlayer player) {
        return new CaptainSlot(proj, player);
    }

}
